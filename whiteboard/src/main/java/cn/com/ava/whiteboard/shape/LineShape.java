package cn.com.ava.whiteboard.shape;

import android.graphics.Canvas;

import cn.com.ava.whiteboard.painttool.PaintTool;

public class LineShape extends Shape {

    @Override
    public void draw(PaintTool paintTool, Canvas canvas) {
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, paintTool.getPaint());
    }


    @Override
    public void touchMove(float x, float y) {
        mEndX = x;
        mEndY = y;
    }

}
