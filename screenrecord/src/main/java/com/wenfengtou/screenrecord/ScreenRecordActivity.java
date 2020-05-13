package com.wenfengtou.screenrecord;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class ScreenRecordActivity extends AppCompatActivity {

    private MicRecorder mMicRecorder;
    private AudioEncoder mAudioEncoder;
    private AudioConfig mAudioConfig;

    private ScreenRecorder mScreenRecorder;
    private static final int REQUEST_SCREEN_RECORD = 1;

    private MediaProjectionManager mMediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_record);

        AndPermission.with(this)
                .runtime()
                .permission(new String[]{Permission.RECORD_AUDIO})
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        /*
                        mAudioConfig = new AudioConfig(44100, AudioFormat.ENCODING_PCM_16BIT, AudioFormat.CHANNEL_IN_MONO);
                        mMicRecorder = new MicRecorder(mAudioConfig);
                        mAudioEncoder = new AudioEncoder(mAudioConfig);
                        mAudioEncoder.start();
                        mMicRecorder.addCallback(mAudioEncoder);
                        mMicRecorder.start();
                         */
                        mScreenRecorder = new ScreenRecorder(ScreenRecordActivity.this);
                        mMediaProjectionManager = mScreenRecorder.getMediaProjectionManager();
                        Intent intent = mMediaProjectionManager.createScreenCaptureIntent();
                        startActivityForResult(intent, REQUEST_SCREEN_RECORD);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCREEN_RECORD) {
            mScreenRecorder.start(resultCode, data);
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioEncoder != null) {
            mAudioEncoder.release();
        }
        if (mMicRecorder != null) {
            mMicRecorder.release();
        }

        if (mScreenRecorder != null) {
            mScreenRecorder.release();
        }
    }
}
