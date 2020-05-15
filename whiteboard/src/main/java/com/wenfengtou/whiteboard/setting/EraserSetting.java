package com.wenfengtou.whiteboard.setting;

import android.graphics.Color;
import android.graphics.PorterDuff;

public class EraserSetting extends PaintSetting {

    private static EraserSetting mInstance;

    public static EraserSetting getInstance() {
        if (mInstance == null) {
            mInstance = new EraserSetting();
            mInstance.reset();
        }
        return mInstance;
    }

    public void reset() {
        mPorterDuffMode = PorterDuff.Mode.CLEAR;
        mStrokeWidth = 30;
        mColor = Color.TRANSPARENT;
    }
}