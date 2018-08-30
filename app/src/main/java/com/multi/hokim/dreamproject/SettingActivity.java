package com.multi.hokim.dreamproject;

/**
 * MainActivity
 */

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.multi.hokim.dreamproject.alarm.MyReceiver;
import com.multi.hokim.dreamproject.alarm.MyService;

public class SettingActivity extends AppCompatActivity {

    TextView textView;
    Switch sw3;
    Switch push;
    String[] items = {"나눔바른펜","나눔스퀘어라운드OTF Bold" ,"나눔스퀘어라운드OTF ExtraBold","나눔스퀘어라운드OTF Regular"};
    private  static MediaPlayer mp;

    private static int ONE_MINUTE = 5626;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent i = new Intent(this, MyService.class);
        startService(i);

        // push
        MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.diary.notification");
        registerReceiver(receiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(this);
        }

        //push알람
        Switch push = (Switch) findViewById(R.id.push);
        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    //푸시알람
                    Toast.makeText(SettingActivity.this, "push알람 off ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SettingActivity.this, MyService.class);
                    stopService(i);
                } else {
                    Toast.makeText(SettingActivity.this, "push알람 on", Toast.LENGTH_SHORT).show();

                }
            }
        });


        //초기화
        Switch sw3 = (Switch) findViewById(R.id.sw3);
        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    //초기화
                    Toast.makeText(SettingActivity.this, "초기화되었습니다", Toast.LENGTH_SHORT).show();

                } else {

                }
            }
        });


        //bgm
        Switch music = (Switch) findViewById(R.id.music);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    mp.pause();
                    Toast.makeText(SettingActivity.this, "Music Stop", Toast.LENGTH_SHORT).show();
                } else {
                    mp.start();
                    Toast.makeText(SettingActivity.this, "Music Start", Toast.LENGTH_SHORT).show();
                }
            }


        });
        mp = MediaPlayer.create(this, R.raw.main_bgm);
        mp.setLooping(true);
        mp.start();

        //폰트변경 스피너

        Spinner spinner = (Spinner) findViewById(R.id.spiner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //아이템 선택 이벤트 처리
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //아이템이 선택되었을 때
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Application app = getApplication();
                if (position == 0) {
                    Toast.makeText(SettingActivity.this, "나눔고딕", Toast.LENGTH_SHORT).show();

                } else if (position == 1) {
                    Toast.makeText(SettingActivity.this, "나눔스퀘어라운드OTF Bold", Toast.LENGTH_SHORT).show();
                    app.setTheme(R.style.CustomTextView);
                } else if (position == 2) {
                    Toast.makeText(SettingActivity.this, "나눔스퀘어라운드OTF ExtraBold", Toast.LENGTH_SHORT).show();
                    app.setTheme(R.style.CustomTextView3);
                } else {
                    Toast.makeText(SettingActivity.this, "나눔스퀘어라운드OTF Regular", Toast.LENGTH_SHORT).show();

                }
                // textView.setText(items[position]);


            }

            //아무것도 선택 안할 때
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView.setText("");

            }
        });
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
