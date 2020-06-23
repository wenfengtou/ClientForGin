package cn.com.ava.whiteboard.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;

//from https://github.com/zengzhaoxing/SharpView
public class SharpDrawable extends GradientDrawable {

    public static final int ARROW_DIRECTION_TOP = 0;
    public static final int ARROW_DIRECTION_BOTTOM = 1;

    private float mSharpSize;

    private int mBgColor;

    private float mCornerRadius;

    private int mArrowDirection;

    private float mBorder;

    private int mBorderColor;

    /**
     * from 0 to 1
     */
    private float mRelativePosition;

    private Paint mPaint;

    private RectF mRect;

    private Path mPath;

    private PointF[] mPointFs;

    public SharpDrawable() {
        super();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mRect = new RectF();
        mPointFs = new PointF[3];
        mPath = new Path();
        mPointFs[0] = new PointF();
        mPointFs[1] = new PointF();
        mPointFs[2] = new PointF();
    }

    void setPaint(Paint paint) {
        mPaint = paint;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
        super.setColor(bgColor);
    }

    public void setCornerRadius(float cornerRadius) {
        mCornerRadius = cornerRadius;
        super.setCornerRadius(cornerRadius);
    }

    public void setArrowDirection(int arrowDirection) {
        mArrowDirection = arrowDirection;
    }

    public void setRelativePosition(float relativePosition) {
        mRelativePosition = Math.min(Math.max(relativePosition, 0), 1);
    }

    void setBorder(float border) {
        mBorder = border;
        super.setStroke((int) mBorder, mBorderColor);
    }

    void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        super.setStroke((int) mBorder, mBorderColor);
    }

    public void setSharpSize(float sharpSize) {
        mSharpSize = sharpSize;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mSharpSize == 0) {
            super.draw(canvas);
        } else {
            Rect bounds = getBounds();
            int left = bounds.left;
            int top = bounds.top;
            int right = bounds.right;
            int bottom = bounds.bottom;
            float length;
            switch (mArrowDirection) {
                case ARROW_DIRECTION_TOP:
                    top += mSharpSize;
                    length = mRelativePosition * bounds.width();
                    mPointFs[0].set(bounds.left + length, bounds.top + mSharpSize * 0.3f);
                    mPointFs[1].set(mPointFs[0].x, top);
                    mPointFs[2].set(mPointFs[0].x + mSharpSize, top);
                    mRect.set(left, top, right, bottom);
                    break;
                case ARROW_DIRECTION_BOTTOM:
                    bottom -= mSharpSize;
                    length = mRelativePosition * bounds.width();
                    mPointFs[0].set(bounds.left + length, bounds.bottom - mSharpSize * 0.3f);
                    mPointFs[1].set(mPointFs[0].x, bottom);
                    mPointFs[2].set(mPointFs[0].x + mSharpSize, bottom);
                    mRect.set(left, top, right, bottom);
                    break;
            }
            mPath.reset();
            mPath.addRoundRect(mRect, mCornerRadius, mCornerRadius, Path.Direction.CW);
            mPath.moveTo(mPointFs[0].x, mPointFs[0].y);
            mPath.lineTo(mPointFs[1].x, mPointFs[1].y);
            mPath.lineTo(mPointFs[2].x, mPointFs[2].y);
            mPath.lineTo(mPointFs[0].x, mPointFs[0].y);
            mPaint.setColor(mBgColor);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawPath(mPath, mPaint);
        }
    }

}
