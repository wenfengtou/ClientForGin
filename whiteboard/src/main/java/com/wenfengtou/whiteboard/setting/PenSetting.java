package com.wenfengtou.whiteboard.setting;

import android.graphics.Color;
import android.graphics.PorterDuff;

public class PenSetting extends PaintSetting {

    private static PenSetting mInstance;

    public static PenSetting getInstance() {
        if (mInstance == null) {
            mInstance = new PenSetting();
            mInstance.reset();
        }
        return mInstance;
    }

    public void reset() {
        mMaxStrokeWidth = 30;
        mPorterDuffMode = null;
        mStrokeWidth = 15;
        mColor = Color.RED;
    }
}

