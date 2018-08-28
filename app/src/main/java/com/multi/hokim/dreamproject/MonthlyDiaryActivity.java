package com.multi.hokim.dreamproject;

/**
 *  먼슬리 화면
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multi.hokim.dreamproject.calendarDecorators.EventDecorator;
import com.multi.hokim.dreamproject.calendarDecorators.SaturdayDecorator;
import com.multi.hokim.dreamproject.calendarDecorators.SundayDecorator;
import com.multi.hokim.dreamproject.dbManager.DBOpenHelper;
import com.multi.hokim.dreamproject.dbManager.DiaryDBCtrct;
import com.multi.hokim.dreamproject.dbManager.TagDiaryRelationsDBCtrct;
import com.multi.hokim.dreamproject.diaryList.DiaryVO;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MonthlyDiaryActivity extends AppCompatActivity implements OnDateSelectedListener, HashTagHelper.OnHashTagClickListener {
    private static final String TAG = MonthlyDiaryActivity.class.getSimpleName();

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private CalendarDay selectedDay;

    private MaterialCalendarView calendarView;
    private LinearLayout diary_preview;
    private TextView date_viewer, body_viewer, hashtag_viewer;
    private Button write_daily_btn;

    private DBOpenHelper dbHelper;
    private HashTagHelper mTextHashTagHelper;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_diary);

        // init sqlite db helper
        dbHelper = new DBOpenHelper(this);

        // set calendaryView
        calendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(this);
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        diary_preview = (LinearLayout)findViewById(R.id.diary_preview);
        date_viewer = (TextView)findViewById(R.id.monthly_date_viewer);
        body_viewer = (TextView)findViewById(R.id.monthly_body_viewer);
        hashtag_viewer = (TextView)findViewById(R.id.monthly_hashtag_viewer);
        write_daily_btn = (Button)findViewById(R.id.write_btn);
        write_daily_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MonthlyDiaryActivity.this, DailyDiaryActivity.class);
                i.putExtra("date", DiaryVO.DATE_FORMAT.format(selectedDay.getDate()));
                startActivity(i);
            }
        });

        char[] additionalSymbols = new char[] {'_','$'};
        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorAccent), this, additionalSymbols);
        mTextHashTagHelper.handle(body_viewer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new DecoratorAsync().execute();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
        diary_preview.setVisibility(View.VISIBLE);

        date_viewer.setText(b ?  FORMATTER.format(calendarDay.getDate()): "");
        selectedDay = calendarDay;

        new DateAsync().execute(calendarDay);
    }

    @Override
    public void onHashTagClicked(String hashTag) {
        Log.v(TAG, "onHashTagClicked["+hashTag+"]");
        // TODO: 해몽 액티비티로 넘어가기

        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MonthlyDiaryActivity.this, hashTag, Toast.LENGTH_SHORT);
        mToast.show();
    }

    // 일기가 입력된 경우를 찾아서 동그라미 표시해주기
    private class DecoratorAsync extends AsyncTask<Void, Void, List<CalendarDay>> {
        private SQLiteDatabase db;

        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            List<CalendarDay> calendarDays = new ArrayList<CalendarDay>();
            Cursor cursor = db.rawQuery(DiaryDBCtrct.SQL_SELECT_DATE, null);

            if(cursor != null) {
                while(cursor.moveToNext()) {
                    String date_s = cursor.getString(0);
                    CalendarDay date;
                    try {
                        date = CalendarDay.from(DiaryVO.DATE_FORMAT.parse(date_s));
                        calendarDays.add(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            return calendarDays;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            db = dbHelper.getReadableDatabase();
        }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if(isFinishing()) {
                return;
            }
            calendarView.addDecorator(new EventDecorator(R.color.colorHighlight, calendarDays));
        }
    }

    // Date로 일기 검색. 일기가 있는 경우 일기를 보여주기
    private class DateAsync extends AsyncTask<CalendarDay, Void, DiaryVO> {
        private SQLiteDatabase db;

        @Override
        protected DiaryVO doInBackground(CalendarDay... calendarDays) {
            String date_s = DiaryVO.DATE_FORMAT.format(calendarDays[0].getDate());
            // Get diary contents
            Cursor cursor = db.rawQuery(
                    DiaryDBCtrct.SQL_SELECT +
                            " WHERE " + DiaryDBCtrct.COL_DATE + " = '" + date_s + "'",
                    null);
            int id = 0;
            String body = "";
            if(cursor.moveToFirst()) {
                id = cursor.getInt(0);
                body = cursor.getString(2);
            }

            // Get hashtags
            ArrayList<String> hashtags = new ArrayList<>();
            cursor = null;
            cursor = db.rawQuery(
                    TagDiaryRelationsDBCtrct.SQL_SELECT_BY_DATE + "'" + date_s + "'",
                    null);
            if(cursor != null) {
                while(cursor.moveToNext()) {
                    hashtags.add(cursor.getString(0));
                }
            }

            return new DiaryVO(id, calendarDays[0], body, hashtags);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = dbHelper.getReadableDatabase();
        }

        @Override
        protected void onPostExecute(DiaryVO diaryVO) {
            super.onPostExecute(diaryVO);
            String hashtags = "";
            for(String tag: diaryVO.getHashtag()) {
                hashtags = hashtags + "#" + tag + " ";
            }

            body_viewer.setText(diaryVO.getBody());
            hashtag_viewer.setText(hashtags);
        }
    }
}
