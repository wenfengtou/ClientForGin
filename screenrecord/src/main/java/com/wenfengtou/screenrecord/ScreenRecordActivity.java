package com.wenfengtou.screenrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioFormat;
import android.os.Bundle;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.security.Permissions;
import java.util.List;

public class ScreenRecordActivity extends AppCompatActivity {

    private MicRecorder mMicRecorder;
    private AudioEncoder mAudioEncoder;
    private AudioConfig mAudioConfig;
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
                        mAudioConfig = new AudioConfig(44100, AudioFormat.ENCODING_PCM_16BIT, AudioFormat.CHANNEL_IN_MONO);
                        mMicRecorder = new MicRecorder(mAudioConfig);
                        mAudioEncoder = new AudioEncoder(mAudioConfig);
                        mAudioEncoder.start();
                        mMicRecorder.addCallback(mAudioEncoder);
                        mMicRecorder.start();
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
    protected void onDestroy() {
        super.onDestroy();
        mAudioEncoder.release();
        mMicRecorder.release();
    }
}
