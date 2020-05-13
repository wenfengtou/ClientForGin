package com.wenfengtou.screenrecord;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.os.Handler;
import android.view.Surface;

import com.wenfengtou.commonutil.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ScreenRecorder {

    private MediaProjectionManager mMediaProjectionManager;
    private MediaCodec mMediaCodec;
    private Surface mSurface;
    private Thread mScreenThread;
    private int mTimeoutUs = 100;
    private boolean mIsStart = false;
    private String mScreenPath = Environment.getExternalStorageDirectory() + File.separator + "ScreenRecord.h264";

    public ScreenRecorder(Context context) {
        init(context);
    }
    private void init(Context context) {
        mMediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        initMediaCodec();
    }

    private void initMediaCodec() {
        try {
            mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            MediaFormat mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 1920, 1080);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1024 * 1024);
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mSurface = mMediaCodec.createInputSurface();
            mMediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public MediaProjectionManager getMediaProjectionManager() {
        return mMediaProjectionManager;
    }

    public void start(int resultCode, Intent data) {
        new File(mScreenPath).delete();
        MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        mediaProjection.registerCallback(new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
            }
        }, new Handler());
        VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay("wenfeng-display", 1920, 1080, 1 /*dpi*/,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                null /*surface*/, null, null);
        virtualDisplay.setSurface(mSurface);
        mScreenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsStart && (!Thread.interrupted())) {
                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    int outputIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, mTimeoutUs);
                    while (outputIndex >= 0) {
                        ByteBuffer byteBuffer = mMediaCodec.getOutputBuffer(outputIndex);
                        byteBuffer.position(bufferInfo.offset);
                        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                        byte[] data = new byte[bufferInfo.size];
                        byteBuffer.get(data);
                        FileUtil.writeFile(data, mScreenPath);
                        mMediaCodec.releaseOutputBuffer(outputIndex, false);
                        outputIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, mTimeoutUs);
                    }
                }
            }
        });
        mIsStart = true;
        mScreenThread.start();
    }

    public void release() {
        mIsStart = false;
    }
}
