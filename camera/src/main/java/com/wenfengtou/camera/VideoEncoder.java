package com.wenfengtou.camera;

import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaFormat;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class VideoEncoder implements WfCamera.FrameCallBack {

    private static final String MIME_TYPE = MediaFormat.MIMETYPE_VIDEO_AVC;
    private LinkedBlockingQueue<byte[]> mVideoDataQueue = new LinkedBlockingQueue<>();
    private boolean mIsEncoding = false;
    private Thread mEncodeThread;
    private MediaCodec mMediaCodec;

    public void startEncode() {
        mEncodeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mIsEncoding = true;
                while (mIsEncoding && (!Thread.interrupted())) {


                }
            }
        });
        mEncodeThread.start();
    }


    private void initMediaCodec() {
        try {
            mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewFrame(byte[] bytes, Camera camera) {
        mVideoDataQueue.add(bytes);
    }
}
