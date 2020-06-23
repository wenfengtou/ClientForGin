package cn.com.ava.whiteboard.setting;

import android.content.Context;

import cn.com.ava.whiteboard.R;

public class PenSetting extends PaintSetting {

    private static PenSetting mInstance;

    private PenSetting(Context context) {
        super(context);
    }

    public static PenSetting getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PenSetting(context);
            mInstance.reset();
        }
        return mInstance;
    }

    public void reset() {
        mPorterDuffMode = null;
        mStrokeWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.whiteboard_5dp);
        mColor = mContext.getResources().getColor(R.color.whiteboard_ff3434);
    }
}

