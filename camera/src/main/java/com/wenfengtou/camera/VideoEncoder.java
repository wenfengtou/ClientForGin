package com.wenfengtou.camera;

import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;

import com.wenfengtou.commonutil.ColorFormatUtil;
import com.wenfengtou.commonutil.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

public class VideoEncoder implements WfCamera.FrameCallBack {

    private static final String TAG = "VideoEncoder";
    private static final String MIME_TYPE = MediaFormat.MIMETYPE_VIDEO_AVC;
    private LinkedBlockingQueue<byte[]> mVideoDataQueue = new LinkedBlockingQueue<>();
    private boolean mIsEncoding = false;
    private Thread mEncodeThread;
    private MediaCodec mMediaCodec;
    private int mWidth;
    private int mHeight;
    private int mFrameRate = 25;
    private int TIMEOUT_USEC = 12000;

    private MediaMuxer mMediaMuxer;

    private static final String H264_PATH = Environment.getExternalStorageDirectory() + File.separator + "Camera.h264";
    private static final String MP4_PATH = Environment.getExternalStorageDirectory() + File.separator + "Camera.mp4";

    private int mVideoIndex;

    public VideoEncoder(int width, int height) {
        mWidth = width;
        mHeight = height;
        new File(H264_PATH).delete();
        new File(MP4_PATH).delete();
        try {
            mMediaMuxer = new MediaMuxer(MP4_PATH, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startEncode() {
        startMediaCodec();
        mEncodeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mIsEncoding = true;
                int ptsIndex = 1;
                while (mIsEncoding && (!Thread.interrupted())) {
                    try {
                        byte[] videoData = mVideoDataQueue.take();
                        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(-1);
                        if (inputBufferIndex >= 0) {
                            long pts = computePresentationTime(ptsIndex);
                            ptsIndex++;
                            ByteBuffer inBuffer = mMediaCodec.getInputBuffer(inputBufferIndex);
                            inBuffer.put(videoData);
                            mMediaCodec.queueInputBuffer(inputBufferIndex, 0, videoData.length, pts, 0);
                        }

                        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                        int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);

                        if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                            MediaFormat mediaFormat = mMediaCodec.getOutputFormat();
                            mVideoIndex = mMediaMuxer.addTrack(mediaFormat);
                            mMediaMuxer.start();
                        }
                        while (outputBufferIndex >= 0) {
                            ByteBuffer outBuffer = mMediaCodec.getOutputBuffer(outputBufferIndex);
                            byte[] outbytes = new byte[bufferInfo.size];
                            outBuffer.get(outbytes);
                            FileUtil.writeH264(outbytes, H264_PATH);
                            Log.i(TAG, "getOutBytes = " + (outbytes[4] & 0x1F));
                            mMediaMuxer.writeSampleData(mVideoIndex, outBuffer, bufferInfo);
                            mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mMediaMuxer.stop();
                mMediaMuxer.release();
            }
        });
        mEncodeThread.start();
    }

    private void startMediaCodec() {
        try {
            mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, mFrameRate);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 1024 * 1024);
            mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewFrame(byte[] bytes, Camera camera) {
        Log.i(TAG, "onNewFrame");
        byte[] nv12Data = new byte[mWidth * mHeight * 3/2];
        ColorFormatUtil.NV21ToNV12(bytes, nv12Data, mWidth, mHeight);
        mVideoDataQueue.add(nv12Data);
    }

    public void stop() {
        mIsEncoding = false;
        mEncodeThread.interrupt();
    }
    /**
     * Generates the presentation time for frame N, in microseconds.
     */
    private long computePresentationTime(long frameIndex) {
        return 132 + frameIndex * 1000000 / mFrameRate;
    }
}
