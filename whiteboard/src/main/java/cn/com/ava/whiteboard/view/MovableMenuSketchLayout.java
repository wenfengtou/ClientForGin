package cn.com.ava.whiteboard.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class MovableMenuSketchLayout extends ViewGroup {

    private static final String TAG = "MovableMenuSketchView";
    private SketchView mSketchView;
    private SketchMenuView mSketchMenuView;
    private int mWidth;
    private int mHeight;
    private int mChildCount;

    public MovableMenuSketchLayout(Context context) {
        this(context, null);
    }

    public MovableMenuSketchLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovableMenuSketchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                    rect.set(menuLeft, menuTop, menuRight, menuBottom);
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


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getMode(heightMeasureSpec);
        if (mHeight != height || mWidth != width) {
            mWidth = width;
            mHeight = height;
        }
        bindSketchToMenu();
    }

    /**
     * 绑定画板与菜单栏
     */
    private void bindSketchToMenu() {
        mChildCount = getChildCount();
        Log.i(TAG, "mChildCount = " + mChildCount);
        if (mChildCount == 2) {
            if (mSketchView == null) {
                for (int i = 0; i < mChildCount; i++) {
                    View child = getChildAt(i);
                    if (child instanceof SketchView) {
                        mSketchView = (SketchView) child;
                    } else if (child instanceof SketchMenuView) {
                        mSketchMenuView = (SketchMenuView) child;
                    }
                }
                if (mSketchView == null || mSketchMenuView == null) {
                    throw new UnsupportedOperationException("Sketch or SketchMenu null!");
                } else {
                    mSketchMenuView.setSketchView(mSketchView);
                }
            }
        } else {
            throw new UnsupportedOperationException("only support 2 children!");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            float x = ev.getX();
            float y = ev.getY();
            int[] location = new int[2];
            mSketchMenuView.getLocationInWindow(location);
            boolean inRect = (x <= (location[0] + mSketchMenuView.getMeasuredWidth())  && x >= location[0]) && (y <= (location[1] + mSketchMenuView.getMeasuredHeight()) && y >= location[1]);
            if (!inRect) {
                mSketchMenuView.touchDownOutside();
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged");
    }


    @Override
    public void dispatchConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "dispatchConfigurationChanged");
        super.dispatchConfigurationChanged(newConfig);
    }

}
