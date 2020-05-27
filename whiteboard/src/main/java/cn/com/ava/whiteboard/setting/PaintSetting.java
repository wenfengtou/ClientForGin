package cn.com.ava.whiteboard.setting;


import android.graphics.PorterDuff;

/**
 * 绘制的设置，包括颜色，大小，透明度等用户可以设置的选项
 */
public abstract class PaintSetting {

    protected int mMaxStrokeWidth;
    protected int mStrokeWidth;
    protected int mColor;
    protected PorterDuff.Mode mPorterDuffMode;
    protected int mShape;

    public int getMaxStrokeWidth() {
        return mMaxStrokeWidth;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.mStrokeWidth = strokeWidth;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public PorterDuff.Mode getPorterDuffMode() {
        return mPorterDuffMode;
    }

    public void setPorterDuffMode(PorterDuff.Mode porterDuffMode) {
        this.mPorterDuffMode = porterDuffMode;
    }

    public int getShape() {
        return mShape;
    }

    public void setShape(int shape) {
        this.mShape = shape;
    }

    protected abstract void reset();
}
