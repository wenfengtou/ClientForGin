package com.wenfengtou.screenrecord;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import java.util.LinkedList;


public class MicRecorder {

    private AudioRecord mAudioRecord;

    private int mSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate;
    private int mChannelConfig;
    private int mAudioFormat;
    private int mBufferSize;

    private boolean mIsStart = false;
    private Thread mRecordThread;

    private LinkedList<Callback> mCallbackList = new LinkedList<>();

    interface Callback {
        void onNewFrame(byte[] frame);
    }

    public MicRecorder(AudioConfig audioConfig) {
        mSampleRate = audioConfig.getSampleRate();
        mChannelConfig = audioConfig.getChannelConfig();
        mAudioFormat = audioConfig.getFormatConfig();
        init();
    }

    private void init() {
        mBufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, mAudioFormat);
        mAudioRecord = new AudioRecord(mSource, 44100, mChannelConfig, mAudioFormat, mBufferSize);
    }

    public void start() {
        mAudioRecord.startRecording();
        mIsStart = true;
        mRecordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsStart && (!Thread.interrupted())) {
                    byte[] data = new byte[mBufferSize];
                    mAudioRecord.read(data, 0, mBufferSize);
                    if (mCallbackList.size() > 0) {
                        for (Callback callback : mCallbackList) {
                            callback.onNewFrame(data);
                        }
                    }
                }
            }
        });
        mRecordThread.start();
    }

    public void release() {
        mIsStart = false;
        mAudioRecord.stop();
        mAudioRecord.release();
    }

    public void addCallback(Callback callback) {
        mCallbackList.add(callback);
    }
}
