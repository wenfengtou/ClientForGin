package com.wenfengtou.clientforgin;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wenfengtou.clientforgin.Util.HookUtil;
import com.wenfengtou.clientforgin.crash.CrashHander;

public class MyApplication extends Application {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static MyApplication instances;
    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHander());
        HookUtil.hookWindowManagerGlobal();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

    public static MyApplication getInstance() {
        return instances;
    }
}
