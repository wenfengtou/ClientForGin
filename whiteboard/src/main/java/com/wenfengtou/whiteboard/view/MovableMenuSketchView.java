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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.wenfengtou.whiteboard.R;

public class MovableMenuSketchView extends FrameLayout {

    private SketchView mSketchView;
    private SketchMenuView mSketchMenuView;
    private FreeView mFreeView;

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

    private void initView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_movable_menu_sketch, this, true);
        mSketchView = findViewById(R.id.movable_skech);
        mSketchMenuView = findViewById(R.id.movable_menu);
        mFreeView = findViewById(R.id.main_freeview);
        mSketchMenuView.setBackgroundColor(Color.BLUE);
        mSketchMenuView.setSketchView(mSketchView);
        //mSketchMenuView.setOnTouchListener(this);
        mFreeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFreeView.isDrag()) {
                    Toast.makeText(context, "正在拖动",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "正在点击",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
