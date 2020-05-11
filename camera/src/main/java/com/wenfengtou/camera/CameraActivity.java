package com.wenfengtou.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView mSurfaceView;
    WfCamera mCamera;
    VideoEncoder mVideoEncoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mSurfaceView = findViewById(R.id.sv_camera);
        mSurfaceView.getHolder().addCallback(this);
        mCamera = new WfCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        final SurfaceHolder finalSurfaceHolder = surfaceHolder;
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> strings) {
                        mCamera.startPreview(finalSurfaceHolder);
                        mCamera.addCallback(mVideoEncoder);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> strings) {

                    }
                })
                .start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
