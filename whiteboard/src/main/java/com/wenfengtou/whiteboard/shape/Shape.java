package com.wenfengtou.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Path;

import com.wenfengtou.whiteboard.painttool.PaintTool;

public abstract class Shape {

    public static final int SHAPE_TYPE_CURVE = 0;
    public static final int SHAPE_TYPE_LINE = 1;

    float mStartX;
    float mStartY;
    float mEndX;
    float mEndY;

    public abstract void draw(PaintTool paintTool, Canvas canvas);

    public void touchDown(float x, float y) {
        mStartX = x;
        mStartY = y;
    }

    public abstract void touchMove(float x, float y);

    public void touchUp(float x, float y) {
        mEndX = x;
        mEndY = y;
    }

    public static Shape createShapeByType(int shapeType) {
        Shape mShape;
        switch (shapeType) {
            case SHAPE_TYPE_LINE:
                mShape = new LineShape();
                break;
            case SHAPE_TYPE_CURVE:
            default:
                mShape = new CurveShape();
                break;
        }
        return mShape;
    }
}
