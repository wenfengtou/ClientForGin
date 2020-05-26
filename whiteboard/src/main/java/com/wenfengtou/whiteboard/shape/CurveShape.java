package com.wenfengtou.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.wenfengtou.whiteboard.painttool.PaintTool;

public class CurveShape extends Shape {

    Path mPath;
    public void setPath(Path path) {
        this.mPath = path;
    }

    public Path getPath() {
        return mPath;
    }

    @Override
    public void draw(PaintTool paintTool, Canvas canvas) {
        if (mPath != null) {
            Paint paint = paintTool.getPaint();
            canvas.drawPath(mPath, paint);
        }
    }

    @Override
    public void touchDown(float x, float y) {
        super.touchDown(x, y);
        mPath.moveTo(x, y);
    }

    @Override
    public void touchMove(float x, float y) {
        float middleX = (mLastX + x) / 2;
        float middleY = (mLastY + y) / 2;
        //mPath.lineTo(x, y);
        mPath.quadTo(mLastX, mLastY, middleX, middleY);
        super.touchMove(x, y); //放在最后再更新mLastX，mLastY
    }
}
