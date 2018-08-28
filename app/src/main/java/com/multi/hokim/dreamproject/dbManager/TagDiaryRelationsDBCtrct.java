package com.multi.hokim.dreamproject.dbManager;

/**
 *  Related table
 */
public class TagDiaryRelationsDBCtrct {
    private TagDiaryRelationsDBCtrct() {}

    public static final String TBL_RELATIONS = "RELATIONS_T";

    public static final String COL_DIARYID = "DIARY_ID";
    public static final String COL_HASHID = "HASH_ID";

    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_RELATIONS + " " +
            "(" +
                COL_DIARYID + " integer " + ", " +
                COL_HASHID  + " integer " + ", " +
                "foreign key(" + COL_DIARYID + ") references " + DiaryDBCtrct.TBL_DIARY + "("+  DiaryDBCtrct.COL_ID + ") ," +
                "foreign key(" + COL_HASHID + ") references " +  HashtagDBCtrct.TBL_HASHTAG + "("+  HashtagDBCtrct.COL_ID + ")" +
            ")";

    // drop table if exists RELATIONS_T
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_RELATIONS ;

    public static final String SQL_SELECT = "SELECT * FROM " + TBL_RELATIONS;

    // Diary id로 Hashtag 검색
    public static final String SQL_SELECT_BY_ID =
            "SELECT " + HashtagDBCtrct.COL_NAME +
                    " FROM " + HashtagDBCtrct.TBL_HASHTAG +
                    " INNER JOIN " + TBL_RELATIONS + " USING(" + COL_HASHID + ")" +
                    " INNER JOIN " + DiaryDBCtrct.TBL_DIARY + " USING(" + COL_DIARYID + ")" +
                    "WHERE " + COL_DIARYID + "=";

    // Diary date로 Hashtag 검색
    // INNER JOIN albums ON tracks.almubid = albums.albumid;
    public static final String SQL_SELECT_BY_DATE =
            "SELECT " + HashtagDBCtrct.COL_NAME +
                    " FROM " + HashtagDBCtrct.TBL_HASHTAG +
                    " INNER JOIN " + TBL_RELATIONS + " ON " + TBL_RELATIONS + "." + COL_HASHID + "=" +
                    HashtagDBCtrct.TBL_HASHTAG + "." + HashtagDBCtrct.COL_ID + " " +
                    " INNER JOIN " + DiaryDBCtrct.TBL_DIARY + " ON " + TBL_RELATIONS + "." + COL_DIARYID + "=" +
                    DiaryDBCtrct.TBL_DIARY + "." + DiaryDBCtrct.COL_ID + " " +
                    "WHERE " + DiaryDBCtrct.TBL_DIARY + "." + DiaryDBCtrct.COL_DATE + "=";

    // Insert or replace into RELATIONS_T
    public static final String SQL_INSERT = "INSERT INTO " + TBL_RELATIONS + " " +
            "(" + COL_DIARYID + ", " + COL_HASHID + ") VALUES ";

    // Delete from RELATIONS_T
    public static final String SQL_DELETE = "DELETE FROM " + TBL_RELATIONS ;

}
