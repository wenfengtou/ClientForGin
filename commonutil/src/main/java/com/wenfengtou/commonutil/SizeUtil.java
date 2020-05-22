package com.wenfengtou.commonutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class SizeUtil {

    private static final String TAG = "SizeUtil";
    /**
     * 得到屏幕大小
     */
    public static void getAreaScreen(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        display.getSize(outP);
        Log.d(TAG, "AreaScreen:" + " width=" + outP.x + " height=" + outP.y);
    }

    /**
     * 得到应用的大小
     */
    public static void getAreaApplication(Activity context) {
        Rect outRect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        int statusHeight = outRect.top;
        int applicationHeight = outRect.height();
        Log.d(TAG, "AreaApplication:" + " width=" + outRect.width()
                + " height=" + outRect.height() + " top=" + outRect.top
                + " bottom=" + outRect.bottom);
    }

    /**
     * 得到View绘制的大小
     */
    public static void getAreaView(Activity context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        Log.w(TAG, "widthPixel = " + widthPixel + ",heightPixel = " + heightPixel);
    }

    public static int getStatusBarHeight(Activity context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.w(TAG, "getStatusBarHeight = " + result);
        return result;
    }

    public static int getAppScreenHeight(Activity context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        Log.w(TAG, "getAppScreenHeight = " + point.y);
        return point.y;
    }

}
