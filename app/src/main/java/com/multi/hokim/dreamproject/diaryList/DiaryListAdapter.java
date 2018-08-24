package com.multi.hokim.dreamproject.diaryList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.multi.hokim.dreamproject.R;

import java.util.ArrayList;

public class DiaryListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<DiaryVO> dairy_list;

    public DiaryListAdapter(Context context, ArrayList<DiaryVO> dairy_list) {
        this.context = context;
        this.dairy_list = dairy_list;
    }

    @Override
    public int getCount() {
        return this.dairy_list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dairy_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.diary, null);
        }

        return convertView;
    }
}
