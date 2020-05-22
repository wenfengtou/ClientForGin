package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class MovableMenuSketchView extends ViewGroup {

    private SketchView mSketchView;
    private SketchMenuView mSketchMenuView;
    private int mWidth;
    private int mHeight;

    public MovableMenuSketchView(Context context) {
        this(context, null);
    }

    public MovableMenuSketchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovableMenuSketchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = right - left;
        mHeight = bottom - top;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof  SketchMenuView) {
                Rect rect = ((SketchMenuView) view).getCurrentRect();
                if (rect.isEmpty()) {
                    int menuWidth = view.getMeasuredWidth();
                    int menuHeight = view.getMeasuredHeight();

                    int menuLeft = mWidth/2 - menuWidth/2;
                    int menuRight = menuLeft + menuWidth;
                    int menuTop = bottom - 2* menuHeight;
                    int menuBottom = menuTop + menuHeight;
                    view.layout(menuLeft,
                            menuTop,
                            menuRight, menuBottom);
                } else {
                    view.layout(rect.left,
                            rect.top,
                            rect.right, rect.bottom);
                }

            } else {
                view.layout(0,
                        0,
                        mWidth, mHeight);
            }

        }
    }

    private void initView(final Context context) {
        SketchView sketchView = new SketchView(context);
        addView(sketchView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        SketchMenuView sketchMenuView = new SketchMenuView(context);
        addView(sketchMenuView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sketchMenuView.setSketchView(sketchView);
        //mSketchMenuView.setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
