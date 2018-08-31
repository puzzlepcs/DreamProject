package com.multi.hokim.dreamproject.diaryList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.multi.hokim.dreamproject.DailyDiaryActivity;
import com.multi.hokim.dreamproject.MonthlyDiaryActivity;
import com.multi.hokim.dreamproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    Context context;
    int itemlayout;
    ArrayList<DiaryVO> data;
    LayoutInflater inflater;

    public MyAdapter(Context context, int itemlayout, ArrayList<DiaryVO> data){
        this.context=context;
        this.itemlayout=itemlayout;
        this.data=data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(itemlayout, parent, false);
        }

        TextView tv1 = (TextView)convertView.findViewById(R.id.tv1);
        TextView tv2 = (TextView)convertView.findViewById(R.id.tv2);

        tv1.setTextColor(Color.rgb(209,212,255));
        tv2.setTextColor(Color.rgb(255,209,223));

        ListView lv = (ListView)parent;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //TODO 리스트뷰 누르면 날짜를 가지고 데일리 화면으로 이동
                Intent i = new Intent(context, DailyDiaryActivity.class);
                i.putExtra("date", FORMAT.format(data.get(position).getDate().getDate()));
                context.startActivity(i);
            }
        });

        DiaryVO d = data.get(position);


        String s = MonthlyDiaryActivity.FORMATTER.format(d.getDate().getDate());
        tv1.setText(s);
        tv2.setText(d.getBody());

        return convertView;
    }
}
