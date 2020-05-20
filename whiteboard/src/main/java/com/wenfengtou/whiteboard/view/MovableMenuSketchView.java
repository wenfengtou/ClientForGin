package com.wenfengtou.whiteboard.view;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.wenfengtou.whiteboard.R;

public class MovableMenuSketchView extends FrameLayout implements View.OnTouchListener {

    private SketchView mSketchView;
    private SketchMenuView mSketchMenuView;

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

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_movable_menu_sketch, this, true);
        mSketchView = findViewById(R.id.movable_skech);
        mSketchMenuView = findViewById(R.id.movable_menu);
        mSketchMenuView.setBackgroundColor(Color.BLUE);
        mSketchMenuView.setSketchView(mSketchView);
        mSketchMenuView.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}