package cn.com.ava.whiteboard.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import cn.com.ava.whiteboard.R;


public class FloatingService extends Service {

    private static final String TAG = "FloatingService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (isAutoRotate(this)) {
            showAutoRotateDialog();
        } else {
            showSketch();
        }
    }

    private void showAutoRotateDialog() {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("开启了自动转屏，转屏后笔迹会清空！");
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showSketch();
            }
        });
        AlertDialog dialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.setCanceledOnTouchOutside(false);//点击外面区域不会让dialog消失
        dialog.show();
    }

    private void showSketch() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_movable_white_board, null, false);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        windowManager.addView(view, layoutParams);
        view.setBackgroundColor(getFilterColor(30));
    }


    /**
     * 判断是否开启了“屏幕自动旋转”,true则为开启
     * @param context 上下文
     * @return true为开启，false为未开启
     */
    private boolean isAutoRotate(Context context) {
        int gravity = 0;
        try {
            gravity = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        boolean isAutoRotate;
        if (gravity == 1) {
            isAutoRotate = true;
        } else {
            isAutoRotate =false;
        }
        Log.i(TAG, "isAutoRotate = " + isAutoRotate);
        return isAutoRotate;
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
