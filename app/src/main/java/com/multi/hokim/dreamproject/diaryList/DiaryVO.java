package com.multi.hokim.dreamproject.diaryList;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.List;

public class DiaryVO {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private CalendarDay date;
    private String body;
    private List<String> hashtags;
    private int id_;

    public DiaryVO() { }

    public DiaryVO(int id) {
        this.id_ = id;
    }

    public DiaryVO(CalendarDay date) {
        this.date = date;
    }

    public DiaryVO(int id, CalendarDay date, String body) {
        this.id_ = id;
        this.date = date;
        this.body = body;
    }

    public DiaryVO(int id, CalendarDay date, String body, List<String> hashtags) {
        this.id_ = id;
        this.date = date;
        this.body = body;
        this.hashtags = hashtags;
    }

    public int getId() { return id_; }
    public CalendarDay getDate() {
        return date;
    }
    public String getDateString() {
        return DATE_FORMAT.format(date);
    }
    public String getBody() {
        return body;
    }
    public List<String> getHashtag() {
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
    public void setHashtag(List<String> hashtags) {
        this.hashtags = hashtags;
    }
    public boolean equals(DiaryVO d) {
        return (d.getDate().equals(this.date));
    }
}
