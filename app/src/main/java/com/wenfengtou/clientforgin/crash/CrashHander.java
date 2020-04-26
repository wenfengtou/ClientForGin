package com.wenfengtou.clientforgin.crash;

import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

public class CrashHander implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHander";

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.i(TAG, "uncaughtException Thread=" + t.getName() + " e=" + e.toString());
        android.os.Process.killProcess(android.os.Process.myPid());
       // System.exit(1);
       // System.gc();
    }
}
