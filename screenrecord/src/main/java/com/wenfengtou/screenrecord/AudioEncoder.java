package com.wenfengtou.screenrecord;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

import com.wenfengtou.commonutil.CodecUtil;
import com.wenfengtou.commonutil.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioEncoder implements MicRecorder.Callback {

    private static final String TAG = "AudioEncoder";
    private MediaCodec mMediaCodec;
    private int mSampleRate;
    private int mChannelCount = 1;
    private int mBitrate = 96000;
    private BlockingQueue<byte[]> mDataQueue = new LinkedBlockingQueue<>();
    private boolean mIsStart = false;
    private Thread mEncodeThread;
    private int mTimeoutUs = 100;
    private String mAACPath = Environment.getExternalStorageDirectory() + File.separator + "wenfeng.aac";

    public AudioEncoder(AudioConfig audioConfig) {
        mSampleRate = audioConfig.getSampleRate();
        init();
    }


    private void init() {
        initMediaCodec();
    }

    private void initMediaCodec() {
        try {
            mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, mSampleRate, mChannelCount);
            mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, mBitrate);
            mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new File(mAACPath).delete();
        mEncodeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int frameIndex = 1;
                while (mIsStart && (!Thread.interrupted())) {
                    try {
                        byte[] data = mDataQueue.take();
                        frameIndex++;
                        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(mTimeoutUs);
                        if (inputBufferIndex >= 0) {
                            ByteBuffer inputByteBuffer =mMediaCodec.getInputBuffer(inputBufferIndex);
                            inputByteBuffer.clear();
                            inputByteBuffer.put(data);
                            inputByteBuffer.limit(data.length);
                            mMediaCodec.queueInputBuffer(inputBufferIndex, 0, data.length, CodecUtil.computePresentationTime(frameIndex, mSampleRate), 0);
                        }
                        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                        int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, mTimeoutUs);
                        while (outputBufferIndex >= 0) {
                            ByteBuffer outputByteBuffer = mMediaCodec.getOutputBuffer(outputBufferIndex);
                            outputByteBuffer.position(bufferInfo.offset);
                            outputByteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                            byte[] aacData = new byte[bufferInfo.size + 7];
                            CodecUtil.addADTStoPacket(aacData, aacData.length);
                            outputByteBuffer.get(aacData, 7, bufferInfo.size);
                            outputByteBuffer.position(bufferInfo.offset);
                            Log.i(TAG, "write aac");
                            FileUtil.writeFile(aacData, mAACPath);
                            mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, mTimeoutUs);
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        mIsStart = true;
        mEncodeThread.start();

    }

    public void release() {
        mIsStart = false;
    }
    @Override
    public void onNewFrame(byte[] frame) {
        try {
            mDataQueue.put(frame);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
