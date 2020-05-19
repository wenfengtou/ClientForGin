package com.wenfengtou.whiteboard.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.wenfengtou.whiteboard.R;
import com.wenfengtou.whiteboard.view.SketchMenuView;
import com.wenfengtou.whiteboard.view.SketchView;

public class FloatingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //Button button = new Button(this);
        //button.setText("悬浮窗");
        //SketchView sketchView = new SketchView(this);
        //sketchView.setBackgroundColor(Color.TRANSPARENT);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_white_board, null, false);
        SketchView sketchView = view.findViewById(R.id.board_view);
        SketchMenuView sketchMenuView = view.findViewById(R.id.sketch_menu);
        sketchMenuView.setSketchView(sketchView);
        FrameLayout frameLayout = new FrameLayout(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = 1920;
        layoutParams.height = 1050;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        windowManager.addView(view, layoutParams);
        view.setBackgroundColor(getFilterColor(30));
    }

    public int getFilterColor(int blueFilterPercent) {
        int realFilter = blueFilterPercent;
        if (realFilter < 10) {
            realFilter = 10;
        } else if (realFilter > 80) {
            realFilter = 80;
        }
        int a = (int) (realFilter / 80f * 180);
        int r = (int) (200 - (realFilter / 80f) * 190);
        int g = (int) (180 - (realFilter / 80f) * 170);
        int b = (int) (60 - realFilter / 80f * 60);
        return Color.argb(a, r, g, b);
    }
}
