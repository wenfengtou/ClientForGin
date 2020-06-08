package cn.com.ava.whiteboard.setting;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

public class EraserSetting extends PaintSetting {

    private static EraserSetting mInstance;
    private Paint mCirclePaint;

    public static EraserSetting getInstance() {
        if (mInstance == null) {
            mInstance = new EraserSetting();
            mInstance.reset();
        }
        return mInstance;
    }

    public void reset() {
        mPorterDuffMode = PorterDuff.Mode.CLEAR;
        mStrokeWidth = 15;
        mColor = Color.TRANSPARENT;
        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setColor(Color.YELLOW);
        //mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Paint getCirclePaint() {
        return mCirclePaint;
    }
}