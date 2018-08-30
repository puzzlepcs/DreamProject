package com.multi.hokim.dreamproject.dbManager;

import com.multi.hokim.dreamproject.R;

public class DiaryDBCtrct {
    private DiaryDBCtrct() {}

    public static final String TBL_DIARY = "DIARY_T";

    public static final String COL_ID = "DIARY_ID";
    public static final String COL_DATE = "DATE_";
    public static final String COL_BODY = "BODY";

    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_DIARY + " " +
            " (" +
                COL_ID      + " integer primary key autoincrement" + ", " +
                COL_DATE    + " text not null"                       + ", " +
                COL_BODY    + " text"+
            ")" ;

    // Drop table if exists DIARY_T
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_DIARY;

    // Select * from DIARY_T
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_DIARY;

    // Select * from DIARY_T by date
    public static final String SQL_SELECT_BY_DATE = "SELECT * FROM " + TBL_DIARY + " WHERE " + COL_DATE + "=";

    // Select date from DIARY_T
    public static final String SQL_SELECT_DATE = "SELECT " + COL_DATE + " FROM " + TBL_DIARY;

    // Insert or replace into DIARY_T
    public static final String SQL_INSERT = "INSERT INTO " + TBL_DIARY + " " +
            "(" + COL_ID + ", " + COL_DATE + ", " + COL_BODY + ") VALUES ";

    // Insert with default id value
    public static final String SQL_INSERT_DFT = "INSERT INTO " + TBL_DIARY + " " +
            "("  + COL_DATE + ", " + COL_BODY + ") VALUES ";

    // Delete from DIARY_T
    public static final String SQL_DELETE = "DELETE FROM " + TBL_DIARY;

    public static String sql_insert_on_duplicate_key(String id, String date, String oldBody, String newBody) {
        // INSERT INTO students (NAME, email) VALUES ('saltfactory', 'saltfactory@gmail.com')
        //ON DUPLICATE KEY UPDATE name='saltfactory', email='saltfactory@me.com';

        String q = SQL_INSERT + "(" + id + ", '" + date + "', '" + oldBody + "') " +
                "ON DUPLICATE KEY UPDATE " +
                COL_ID + "=" + id + ", " +
                COL_DATE + "='" + date + "', " +
                COL_BODY + "='" + newBody + "'";
        return q;
    }

}

