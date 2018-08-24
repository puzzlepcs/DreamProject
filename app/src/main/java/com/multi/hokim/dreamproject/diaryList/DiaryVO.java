package com.multi.hokim.dreamproject.diaryList;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class DiaryVO {
    private CalendarDay date;
    private String body;
    private ArrayList<String> hashtags;
    private int id_;

    public DiaryVO() { }

    public DiaryVO(int id) {
        this.id_ = id;
    }

    public DiaryVO(int id, CalendarDay date, String body, ArrayList<String> hashtags) {
        this.id_ = id;
        this.date = date;
        this.body = body;
        this.hashtags = hashtags;
    }

    public int getId() { return id_; }
    public CalendarDay getDate() {
        return date;
    }
    public String getBody() {
        return body;
    }
    public ArrayList<String> getHashtag() {
        return hashtags;
    }
    public void setId(int id) {
        this.id_ = id;
    }
    public void setDate(CalendarDay calendarDay) {
        this.date = calendarDay;
    }
    public void setDate(String year, String month, String day) {
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);
        this.date = CalendarDay.from(y, m, d);
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setHashtag(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public boolean equals(DiaryVO d) {
        return (d.getDate().equals(this.date));
    }
}
