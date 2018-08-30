package com.multi.hokim.dreamproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button diarybt = (Button)findViewById(R.id.diarybt);
//        diarybt.setOnClickListener(this);
//        Button chartbt = (Button)findViewById(R.id.chartbt);
//        chartbt.setOnClickListener(this);
//        Button settingbt = (Button)findViewById(R.id.settingbt);

    }


    public void setting(View v) {
        Intent setting = new Intent(this, SettingActivity.class);
        startActivity(setting);
    }

    public void diary(View v) {
        Intent i  = new Intent(this, MonthlyDiaryActivity.class);
        startActivity(i);
    }

    public void chart(View v) {
        Intent i = new Intent(this, TagCloudActivity.class);
        startActivity(i);
    }
}
