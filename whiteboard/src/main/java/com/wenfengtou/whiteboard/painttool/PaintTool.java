package com.wenfengtou.whiteboard.painttool;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wenfengtou.whiteboard.shape.Shape;

public abstract class PaintTool {
    /**
     * 画笔
     */
    Paint mPaint;

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

    protected void drawShape(Canvas canvas, Shape shape) {
        shape.draw(this, canvas);
    }
}
