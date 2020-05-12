package com.wenfengtou.camera;


import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.LinkedList;

public class WfCamera implements Camera.PreviewCallback {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    int mWidth;
    int mHeight;
    LinkedList<FrameCallBack> mFrameCallBacks = new LinkedList<>();

    public WfCamera(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    interface FrameCallBack {
        void onNewFrame(byte[] bytes, Camera camera);
    }

    public void addCallback(FrameCallBack frameCallBack) {
        mFrameCallBacks.add(frameCallBack);
    }

    public void startPreview(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        open();
        setParameter();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);
    }

    public void open() {
        mCamera = Camera.open(0);
    }

    public void setParameter() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setPreviewSize(mWidth, mHeight);
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        for(FrameCallBack frameCallBack : mFrameCallBacks) {
            frameCallBack.onNewFrame(bytes, camera);
        }
    }
}
