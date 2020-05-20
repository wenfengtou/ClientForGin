package com.wenfengtou.whiteboard.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class PenImageView extends ImageView {
    public PenImageView(Context context) {
        super(context);
    }

    public PenImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PenImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
