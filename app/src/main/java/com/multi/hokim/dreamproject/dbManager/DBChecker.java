package com.multi.hokim.dreamproject.dbManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBChecker {
    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public DBChecker(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public void checkHash() {
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(HashtagDBCtrct.SQL_SELECT, null);
        String msg = "\n";
        if(cursor != null) {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);

                msg += "[id " + id + ", name " + name + "]\n";
            }
        }
        Log.e("HASH_T", msg);
    }

    public void checkDiary() {
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(DiaryDBCtrct.SQL_SELECT, null);
        String msg = "\n";
        if(cursor != null) {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String body = cursor.getString(2);

                msg += "[id " + id + ", date " + date + ", body " + body + "]\n";
            }
        }
        Log.e("DIARY_T", msg);
    }

    public void checkRelation() {
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(TagDiaryRelationsDBCtrct.SQL_SELECT, null);
        String msg = "\n";
        if(cursor != null) {
            while(cursor.moveToNext()) {
                int diary_id = cursor.getInt(0);
                int hash_id = cursor.getInt(1);

                msg += "[diary_id " + diary_id + ", hash_id " + hash_id + "]\n";
            }
        }
        Log.e("RELATIONS_T", msg);
    }
}
