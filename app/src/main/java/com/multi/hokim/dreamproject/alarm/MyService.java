package com.multi.hokim.dreamproject.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;

public class MyService extends Service {
    final String tag = "MyService Tag:";
    AlarmManager alarmManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //Service 시작 후 인텐트 도착시 호출되는 메소드
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent Intent = new Intent("com.diary.notification");
        PendingIntent pIntent = PendingIntent.getBroadcast(
                this, 0, Intent, 0);

        //알람 울릴시간 지정
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 9);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                time.getTimeInMillis(),
                //   System.currentTimeMillis() + 5000,
                pIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
