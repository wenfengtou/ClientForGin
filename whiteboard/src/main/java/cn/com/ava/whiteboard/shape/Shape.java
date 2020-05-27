package cn.com.ava.whiteboard.shape;

import android.graphics.Canvas;

import cn.com.ava.whiteboard.painttool.PaintTool;

public abstract class Shape {

    public static final int SHAPE_TYPE_CURVE = 0;
    public static final int SHAPE_TYPE_LINE = 1;

    //起点
    float mStartX;
    float mStartY;
    //过程点
    float mLastX;
    float mLastY;
    //结束点
    float mEndX;
    float mEndY;

    public abstract void draw(PaintTool paintTool, Canvas canvas);

    public void touchDown(float x, float y) {
        mStartX = x;
        mStartY = y;
        mLastX = x;
        mLastY = y;
    }

    public void touchMove(float x, float y) {
        mLastX = x;
        mLastY = y;
    }

    public void touchUp(float x, float y) {
        mEndX = x;
        mEndY = y;
        mLastX = x;
        mLastY = y;
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
