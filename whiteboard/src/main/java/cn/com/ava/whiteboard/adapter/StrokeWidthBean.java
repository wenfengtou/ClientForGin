package cn.com.ava.whiteboard.adapter;

public class StrokeWidthBean {

    public int pressedDrawableId;
    public int unPressedDrawableId;
    public int strokeWidth;

    public StrokeWidthBean(int pressedDrawableId, int unPressedDrawableId, int strokeWidth) {
        this.pressedDrawableId = pressedDrawableId;
        this.unPressedDrawableId = unPressedDrawableId;
        this.strokeWidth = strokeWidth;
    }
}
