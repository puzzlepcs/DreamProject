package com.multi.hokim.dreamproject;

/**
 *  데일리 화면
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.multi.hokim.dreamproject.dbManager.DBChecker;
import com.multi.hokim.dreamproject.dbManager.DBOpenHelper;
import com.multi.hokim.dreamproject.dbManager.DiaryDBCtrct;
import com.multi.hokim.dreamproject.dbManager.HashtagDBCtrct;
import com.multi.hokim.dreamproject.dbManager.TagDiaryRelationsDBCtrct;
import com.multi.hokim.dreamproject.diaryList.DiaryVO;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DailyDiaryActivity extends AppCompatActivity implements HashTagHelper.OnHashTagClickListener, View.OnClickListener {
    private static final String TAG = DailyDiaryActivity.class.getSimpleName();

    private DBOpenHelper dbHelper;
    private HashTagHelper mEditTextHashTagHelper;

    private CalendarDay selectedDate;
    private String selectedDate_s;
    private DiaryVO diaryVO;

    private TextView date_viewer;
    private EditText mEditTextView;
    private TextView mAllHashTag;

    private Toast mToast;

    private DBChecker dbChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_diary);

        dbHelper = new DBOpenHelper(this);
        dbChecker = new DBChecker(this);

        Intent i = getIntent();
        selectedDate_s = i.getCharSequenceExtra("date").toString();
        try {
            selectedDate = CalendarDay.from(DiaryVO.DATE_FORMAT.parse(selectedDate_s));
            Log.e(TAG, "selectedDate = " + selectedDate_s);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Parsing failed");
        }

        date_viewer = (TextView) findViewById(R.id.daily_date_viewer);
        date_viewer.setText(selectedDate_s);
        mAllHashTag = (TextView) findViewById(R.id.all_hashtags);
        mEditTextView = (EditText) findViewById(R.id.edit_text_field);

        Button dailySaveBtn = (Button) findViewById(R.id.daily_save_btn);
        dailySaveBtn.setOnClickListener(this);

        char[] additionalSymbols = new char[]{'_', '$'};
        mEditTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorAccent), this, additionalSymbols);
        mEditTextHashTagHelper.handle(mEditTextView);

        load_contents();
    }

    private void load_contents() {
        // 기존의 다이어리가 있다면 내용이 로드 됨.
        // new LoadAsync().execute();

        SQLiteDatabase db;
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                DiaryDBCtrct.SQL_SELECT_BY_DATE + "'" + selectedDate_s + "'",
                null);

        int id = 0;
        String body = "";
        String msg = "";
        if (cursor != null) {
            while (cursor.moveToNext()) {
                id = cursor.getInt(0);
                msg += "[diary_id: " + id + "], ";
                String date = cursor.getString(1);
                msg += "[date: " + date + "], ";
                body = cursor.getString(2);
                msg += "[body: " + body + "]";
            }
        }
        diaryVO = new DiaryVO(id, selectedDate, body);
        diaryVO.setHashtag(mEditTextHashTagHelper.getAllHashTags());

        mEditTextView.setText(body);

        Log.e(TAG, "Load contents done" + msg);

        dbChecker.checkDiary();
        dbChecker.checkHash();
        dbChecker.checkRelation();
    }

    @Override
    public void onHashTagClicked(String hashTag) {
        Log.v(TAG, "onHashTagClicked[" + hashTag + "]");
        // TODO: 해몽 액티비티로 넘어가기
        //  Intent 생성후 해시태그 넘겨주기

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(DailyDiaryActivity.this, hashTag, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daily_save_btn:
                new SaveAsync().execute();
                break;
        }
    }

    // Save the contents - date, body to diary_t, hashtag
    private class SaveAsync extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase db;

        @Override
        protected Void doInBackground(Void... voids) {
            if(diaryVO.getId() == 0) {
                db.execSQL(
                        DiaryDBCtrct.SQL_INSERT_DFT + "('" + selectedDate_s + "', '" + diaryVO.getBody() + "')");

                int diary_id = getDiaryId();
                int hash_id = 0;
                for(String h : diaryVO.getHashtag()) {
                    hash_id = getHashId(h);
                    if(hash_id == 0) {
                        db.execSQL(HashtagDBCtrct.SQL_INSERT_HASH + "('" + h + "')");
                        hash_id = getHashId(h);
                    }

                    db.execSQL(
                            TagDiaryRelationsDBCtrct.SQL_INSERT + "(" + diary_id + ", " + hash_id +")");
                }

            } else {

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = dbHelper.getWritableDatabase();

            // 수정, 입력받을 내용을 diaryVO 객체에 저장
            String body = mEditTextView.getText().toString();
            ArrayList<String> hashtags = (ArrayList<String>) mEditTextHashTagHelper.getAllHashTags();
            diaryVO.setBody(body);
            diaryVO.setHashtag(hashtags);

            Log.e(TAG, "SaveAsync ready");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        private int getDiaryId() {
            int id = 0;
            Cursor cursor = db.rawQuery(
                    DiaryDBCtrct.SQL_SELECT_BY_DATE + "'" + selectedDate_s + "'", null);

            if(cursor != null) {
                while(cursor.moveToNext()) {
                    id = cursor.getInt(0);
                }
            }

            return id;
        }

        private int getHashId(String name) {
            int id = 0;
            Cursor cursor = db.rawQuery(
                    HashtagDBCtrct.SQL_SELECT + " WHERE " + HashtagDBCtrct.COL_NAME + "='" + name + "'" ,
                    null);

            if(cursor != null) {
                while(cursor.moveToNext()) {
                    id = cursor.getInt(0);
                }
            }

            return id;
        }

    }
}
    // Load the contents of the selected diary
    // create an instance of DiaryVO if the diary already exists


/**
 * // [STEP-1] 다이어리 테이블, 릴레이션 테이블
 // 새로운 다이어리 입력한 경우
 if (diaryVO == null) {
 Log.e(TAG, "diary Not found");
 // DIARY_T에 값 넣어주기
 String diary_q = DiaryDBCtrct.SQL_INSERT + "(" +
 diary_id + ", " +
 "'" + selectedDate_s + "', " +
 "'" + newDiary.getBody() + "'"+
 ")";
 db.execSQL(diary_q);

 // HASH_T와 RELATION_T에 값 넣어주기
 for(String h : newDiary.getHashtag()) {
 Cursor cursor = db.rawQuery(
 HashtagDBCtrct.SQL_SELECT + " WHERE " + HashtagDBCtrct.COL_NAME + "='" + h + "'",
 null);
 int hash_id = 0;
 if(cursor == null) {
 // 결과가 없으면 새로운 해시태그 생성해주고 id값 받아오기
 db.execSQL(
 HashtagDBCtrct.SQL_INSERT_HASH + "(" + h + ")",
 null);
 Cursor c = db.rawQuery(HashtagDBCtrct.SQL_SELECT + " WHERE " + HashtagDBCtrct.COL_NAME + "='" + h + "'",
 null);
 while (c.moveToNext()) {
 hash_id = c.getInt(0);
 }
 } else  {
 // 이미 있는 해시태그인 경우 id 값 받기;
 while(cursor.moveToNext()) {
 hash_id = cursor.getInt(0);
 }
 }

 db.execSQL(TagDiaryRelationsDBCtrct.SQL_INSERT + "(" + newDiary.getId() + ", " + hash_id + ")");
 }

 Log.e(TAG, "insertion completed");
 }
 // 기존의 다이어리를 수정한 경우
 else {

 }

 private class LoadAsync extends AsyncTask<Void, Void, Void> {
 private SQLiteDatabase db;

 @Override
 protected  Void doInBackground(Void... voids) {
 // TODO: load the contents of the diary
 Cursor cursor = db.rawQuery(
 DiaryDBCtrct.SQL_SELECT_BY_DATE + "'" + selectedDate_s + "'",
 null);

 int id = 0;
 String body = "";
 String msg = "";
 if(cursor != null) {
 while (cursor.moveToNext()) {
 id  = cursor.getInt(0);
 msg += "[diary_id: " + id + "], ";
 String date = cursor.getString(1);
 msg += "[date: "+ date + "], ";
 body = cursor.getString(2);
 msg += "[body: " + body + "]";
 diaryVO = new DiaryVO();
 diaryVO.setBody(body);
 diaryVO.setId(id);
 diaryVO.setDate(selectedDate);
 }
 } else {
 diaryVO = null;
 }
 Log.e(TAG, msg);
 return null;
 }

 protected void onPreExecute() {
 super.onPreExecute();
 db = dbHelper.getReadableDatabase();
 }

 protected void onPostExecute(Void aVoid) {
 super.onPostExecute(aVoid);

 if(diaryVO != null) {
 mEditTextView.setText(diaryVO.getBody());
 diaryVO.setHashtag(mEditTextHashTagHelper.getAllHashTags());
 }
 }
 }
 */

