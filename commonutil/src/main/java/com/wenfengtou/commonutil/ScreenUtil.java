package com.wenfengtou.commonutil;

import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class ScreenUtil {

    private static final String TAG = "ScreenUtil";

    // 判断是否开启了 “屏幕自动旋转”,true则为开启
    public static boolean isAutoRotate(Context context) {
        int gravity = 0;
        try {
            gravity = Settings.System.getInt(context.getContentResolver(),git
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return gravity == 1;
    }

    public static void getAndroiodScreenProperty(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        Log.d(TAG, "屏幕宽度（像素）：" + width);
        Log.d(TAG, "屏幕高度（像素）：" + height);
        Log.d(TAG, "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d(TAG, "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d(TAG, "屏幕宽度（dp）：" + screenWidth);
        Log.d(TAG, "屏幕高度（dp）：" + screenHeight);
    }

}
