package com.wenfengtou.clientforgin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    int notificationId = 0;
    Notification.Builder builder = null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MyNotificationManager.initNotificationChannel(this);
            notificationId = 0x1234;
            builder = new Notification.Builder(this,"1");

            builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentText("xxx");

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        startForeground(notificationId, builder.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startForeground(notificationId, builder.build());
    }
}
