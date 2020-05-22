package com.wenfengtou.whiteboard.painttool;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.wenfengtou.whiteboard.setting.PaintSetting;
import com.wenfengtou.whiteboard.shape.Shape;

public abstract class PaintTool {

    public final static int PAINT_TOOL_NONE = -1;
    public final static int PAINT_TOOL_PEN = 0;
    public final static int PAINT_TOOL_ERASER = 1;
    /**
     * 画笔
     */
    Paint mPaint;

    public PaintTool(PaintSetting paintSetting) {
        mPaint = new Paint();
        setDefaultPaintAttr(mPaint);
        mPaint.setStrokeWidth(paintSetting.getStrokeWidth());
        mPaint.setColor(paintSetting.getColor());
        mPaint.setXfermode(paintSetting.getPorterDuffMode() == null ? null : new PorterDuffXfermode(paintSetting.getPorterDuffMode()));
    }

    private void setDefaultPaintAttr(Paint paint) {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }


    public Paint setPaint(Paint paint) {
        return mPaint = paint;
    }

    /**
     * 获取画笔
     * @return 画笔
     */
    public Paint getPaint() {
        return mPaint;
    }

    /**
     * 设置画笔粗细
     * @param size
     */
    public void setStrokeWidth(int size) {
        mPaint.setStrokeWidth(size);
    }

    /**
     * 设置画笔颜色
     * @param color
     */
    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void drawShape(Canvas canvas, Shape shape) {
        shape.draw(this, canvas);
    }

}
