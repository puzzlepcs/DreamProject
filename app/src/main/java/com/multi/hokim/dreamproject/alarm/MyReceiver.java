package com.multi.hokim.dreamproject.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.multi.hokim.dreamproject.R;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context, "noti1")
                .setContentTitle("꿈다")
                .setContentText("오늘 일기 썼나요?")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

        manager.notify(1, builder.build());


    }
}
