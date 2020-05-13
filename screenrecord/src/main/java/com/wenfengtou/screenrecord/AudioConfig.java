package com.wenfengtou.screenrecord;

public class AudioConfig {

    private int mSampleRate;
    private int mFormatConfig;
    private int mChannelConfig;

    public AudioConfig(int mSampleRate, int mFormatConfig, int mChannelConfig) {
        this.mSampleRate = mSampleRate;
        this.mFormatConfig = mFormatConfig;
        this.mChannelConfig = mChannelConfig;
    }

    public int getSampleRate() {
        return mSampleRate;
    }

    public void setSampleRate(int mSampleRate) {
        this.mSampleRate = mSampleRate;
    }

    public int getFormatConfig() {
        return mFormatConfig;
    }

    public void setFormatConfig(int mFormatConfig) {
        this.mFormatConfig = mFormatConfig;
    }

    public int getChannelConfig() {
        return mChannelConfig;
    }

    public void setChannelConfig(int mChannelConfig) {
        this.mChannelConfig = mChannelConfig;
    }
}
