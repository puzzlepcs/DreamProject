package com.multi.hokim.dreamproject;


/**
 * MainActivity2
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.multi.hokim.dreamproject.alarm.*;

public class PushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, MyService.class);
        startService(i);

        MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.diary.notification");
        registerReceiver(receiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(this);
        }

    }

    public void createChannel(Context context) {
        NotificationManager manager =(NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channelComment = new NotificationChannel("ch1",
                "notification_channel_comment_title", NotificationManager.IMPORTANCE_DEFAULT);
        channelComment.setDescription("notification_channel_comment_description");
        channelComment.setLightColor(Color.BLUE);
        channelComment.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        manager.createNotificationChannel(channelComment);

        NotificationChannel channelNotice = new NotificationChannel("noti1",
                "일기쓰기앱", NotificationManager.IMPORTANCE_HIGH);
        channelNotice.setDescription("notification_channel_notice_description");
        channelNotice.setLightColor(Color.RED);
        channelNotice.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        manager.createNotificationChannel(channelNotice);
    }
}
