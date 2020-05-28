package com.wenfengtou.commonutil;

import android.content.Context;
import android.provider.Settings;

public class ScreenUtil {

    // 判断是否开启了 “屏幕自动旋转”,true则为开启
    public static boolean isAutoRotate(Context context) {
        int gravity = 0;
        try {
            gravity = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return gravity == 1;
    }

}
