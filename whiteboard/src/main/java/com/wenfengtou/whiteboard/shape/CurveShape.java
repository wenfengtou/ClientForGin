package com.wenfengtou.whiteboard.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.wenfengtou.whiteboard.painttool.PaintTool;

public class CurveShape extends Shape {

    private Path mPath;

    public void setPath(Path path) {
        this.mPath = path;
    }

    @Override
    public void draw(PaintTool paintTool, Canvas canvas) {
        if (mPath != null) {
            Paint paint = paintTool.getPaint();
            canvas.drawPath(mPath, paint);
        }
    }
}
