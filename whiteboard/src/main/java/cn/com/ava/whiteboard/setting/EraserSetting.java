package cn.com.ava.whiteboard.setting;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import cn.com.ava.whiteboard.R;

public class EraserSetting extends PaintSetting {

    private static EraserSetting mInstance;
    private Paint mCirclePaint;

    private EraserSetting(Context context) {
        super(context);
    }

    public static EraserSetting getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new EraserSetting(context);
            mInstance.reset();
        }
        return mInstance;
    }

    public void reset() {
        mPorterDuffMode = PorterDuff.Mode.CLEAR;
        mStrokeWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.eraser_size2);
        mColor = Color.TRANSPARENT;
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setColor(mContext.getResources().getColor(R.color.whiteboard_size_round_color));
        //mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Paint getCirclePaint() {
        //mCirclePaint.setStrokeWidth(mStrokeWidth);
        return mCirclePaint;
    }
}