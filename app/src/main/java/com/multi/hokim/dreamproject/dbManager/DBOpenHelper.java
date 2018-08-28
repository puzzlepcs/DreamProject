package com.multi.hokim.dreamproject.dbManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


public class DBOpenHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "createTest.db";

    public DBOpenHelper(Context context) {
        /**
         *  수퍼클래스 생성자 param
         *      Context con     : 데이터베이스에 첨부된 컨텍스트
         *      databaseName    : 데이터베이스의 이름
         *      CursorFactory   : 기본 Cursor 이용시 null
         *      Version         : 데이터베이스의 스키마 버전. 지정된 이름과 버전으로 새 데이터베이스 생성
         */
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }else {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DiaryDBCtrct.SQL_CREATE_TBL);
        db.execSQL(HashtagDBCtrct.SQL_CREATE_TBL);
        db.execSQL(TagDiaryRelationsDBCtrct.SQL_CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DiaryDBCtrct.SQL_DROP_TBL);
        db.execSQL(HashtagDBCtrct.SQL_DROP_TBL);
        db.execSQL(TagDiaryRelationsDBCtrct.SQL_DROP_TBL);
        onCreate(db);
    }

}
