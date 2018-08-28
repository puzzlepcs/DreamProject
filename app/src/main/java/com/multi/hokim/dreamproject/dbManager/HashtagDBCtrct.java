package com.multi.hokim.dreamproject.dbManager;

public class HashtagDBCtrct {
    private HashtagDBCtrct() {}

    public static final String TBL_HASHTAG = "HASH_T";

    public static final String COL_ID = "HASH_ID";
    public static final String COL_NAME = "NAME";


    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_HASHTAG + " " +
            "(" +
                COL_ID      + " integer primary key autoincrement" + ", " +
                COL_NAME    + " text not null" +
            ")";

    // drop table if exists HASH_T
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_HASHTAG ;

    // Select * from HASH_T
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_HASHTAG ;

    // Insert or replace into HASH_T
    public static final String SQL_INSERT = "INSERT INTO " + TBL_HASHTAG + " " +
            "(" + COL_ID + ", " + COL_NAME + ") VALUES ";

    public static final String SQL_INSERT_HASH = "INSERT INTO " + TBL_HASHTAG + " " +
            "(" + COL_NAME + ") VALUES ";

    // Delete from HASH_T
    public static final String SQL_DELETE = "DELETE FROM " + TBL_HASHTAG ;

}
