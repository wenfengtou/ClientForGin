package com.wenfengtou.whiteboard.shape;

import android.graphics.Canvas;

import com.wenfengtou.whiteboard.painttool.PaintTool;

public abstract class Shape {
    float mStartX;
    float mStartY;
    float mEndX;
    float mEndY;

    public abstract void draw(PaintTool paintTool, Canvas canvas);
}
