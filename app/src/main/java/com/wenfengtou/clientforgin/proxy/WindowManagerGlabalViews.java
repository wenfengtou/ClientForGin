package com.wenfengtou.clientforgin.proxy;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.wenfengtou.clientforgin.MyApplication;

import java.util.ArrayList;

public class WindowManagerGlabalViews extends ArrayList<View> {

    public ArrayList<View> mBase;
    public WindowManagerGlabalViews(ArrayList<View> base) {
        mBase = base;
    }

    @Override
    public boolean add(View view) {
        if (mBase != null) {
            mBase.add(view);
        }
        ((ViewGroup)view).addView(getNightView(), getWindowManagerLayoutParams());
        return super.add(view);
    }

    private static View getNightView() {
        LinearLayout linearLayout = new LinearLayout(MyApplication.getInstance());
        linearLayout.setFocusable(false);
        linearLayout.setFocusableInTouchMode(false);
        linearLayout.setBackgroundColor(getBlueFilterColor());
        return linearLayout;
    }

    private static int getBlueFilterColor() {
        float f = ((float) 20) / 80.0f;
        return Color.argb((int) (f * 180.0f), (int) (200.0f - (190.0f * f)), (int) (180.0f - (170.0f * f)), (int) (60.0f - (f * 60.0f)));
    }


    private static WindowManager.LayoutParams getWindowManagerLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = 2;
        layoutParams.flags = 256;
        layoutParams.format = 1;
        layoutParams.gravity = 17;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = -1;
        layoutParams.height = -1;
        return layoutParams;
    }

}
