package cn.com.ava.whiteboard.setting;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import cn.com.ava.whiteboard.R;

public class EraserSetting extends PaintSetting {

    private static EraserSetting mInstance;
    private Paint mOutsideCirclePaint;
    private Paint mInsideCirclePaint;

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
        mStrokeWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.whiteboard_31dp);
        mColor = Color.TRANSPARENT;
        mOutsideCirclePaint = new Paint();
        mOutsideCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mOutsideCirclePaint.setColor(mContext.getResources().getColor(R.color.whiteboard_eraser_ball_outside));
        mInsideCirclePaint = new Paint();
        mInsideCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mInsideCirclePaint.setColor(mContext.getResources().getColor(R.color.whiteboard_eraser_ball_inside));
        //mOutsideCirclePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Paint getOutsideCirclePaint() {
        //mOutsideCirclePaint.setStrokeWidth(mStrokeWidth);
        return mOutsideCirclePaint;
    }

    public Paint getInsideCirclePaint() {
        //mOutsideCirclePaint.setStrokeWidth(mStrokeWidth);
        return mInsideCirclePaint;
    }
}