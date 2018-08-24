package com.multi.hokim.dreamproject;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multi.hokim.dreamproject.calendarDecorators.EventDecorator;
import com.multi.hokim.dreamproject.calendarDecorators.SaturdayDecorator;
import com.multi.hokim.dreamproject.calendarDecorators.SundayDecorator;
import com.multi.hokim.dreamproject.diaryList.DiaryVO;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MonthlyDiaryActivity extends AppCompatActivity implements OnDateSelectedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    private MaterialCalendarView calendarView;
    private ArrayList<DiaryVO> samples;
    private LinearLayout diary_preview;
    private TextView date_viewer, body_viewer, hashtag_viewer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_diary);

        // Sample diary data
        /** TODO: 여기서 데이터베이스에서 DiaryVO 가지고 오기
        *   몇달치 가지고 와야할까..?
        */
        ArrayList<String> sampleHashtag = new ArrayList<>();
        sampleHashtag.add("blah");
        sampleHashtag.add("pig");
        DiaryVO sample1 = new DiaryVO(
                0,
                CalendarDay.from(Calendar.getInstance()),
                "dsafkjalafjladjflskdqlasdlaladjfaslkjfalkolijlliljlkb  jjalfjdls ",
                sampleHashtag);
        DiaryVO sample2 = new DiaryVO(
                1,
                CalendarDay.from(2000, 1, 2),
                "body",
                sampleHashtag);
        samples = new ArrayList<>();
        samples.add(sample1);
        samples.add(sample2);

        // set calendaryView
        calendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(this);
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        // start apiSumulator
        new ApiSimulator().execute(samples);

        diary_preview = (LinearLayout)findViewById(R.id.diary_preview);
        date_viewer = (TextView)findViewById(R.id.date_viewer);
        body_viewer = (TextView)findViewById(R.id.body_viewer);
        hashtag_viewer = (TextView)findViewById(R.id.hashtag_viewer);


    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
        // TODO: date 넘겨주고 일기 입력 받는 화면 띄우기
        diary_preview.setVisibility(View.VISIBLE);

        date_viewer.setText(b ?  FORMATTER.format(calendarDay.getDate()): "");
        body_viewer.setText(b ? samples.get(1).getBody() : "");

        ArrayList<String> hashtags = samples.get(1).getHashtag();
        String str = "";
        for(String s: hashtags) {
            str += str + "#"+ s + " ";
        }
        hashtag_viewer.setText(b ? str: "");
    }

    // 주어진 날짜에 동그라미 표시하기
    private class ApiSimulator extends AsyncTask<ArrayList<DiaryVO>, ArrayList<CalendarDay>, ArrayList<CalendarDay>> {
        @Override
        protected void onPreExecute() {
            // TODO: query here and get result form db
        }

        @Override
        protected ArrayList<CalendarDay> doInBackground(ArrayList<DiaryVO>... lists) {

            ArrayList<CalendarDay> dates = new ArrayList<>();
            for(ArrayList<DiaryVO> diaries : lists) {
                for(DiaryVO d : diaries) {
                    dates.add(d.getDate());
                }
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull ArrayList<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if(isFinishing()) {
                return;
            }
            calendarView.addDecorator(new EventDecorator(R.color.colorHighlight, calendarDays));
        }
    }
}
