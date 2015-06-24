package com.vk.fesswod.articleView.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;

/**
 * Created by sergeyb on 17.06.15.
 */
public class AppSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Article.db";
    private static final int DB_VERSION = 6;
    public static final String SQL_INSERT_OR_REPLACE = "__sql_insert_or_replace__";



    public static final String COLUMN_ID = "_id";

    public static final String TABLE_ARTICLE = "ARTICLE";
    public static final String ARTICLES_COLUMN_TITLE = "TITLE";
    public static final String ARTICLES_COLUMN_DESC = "DESC";
    public static final String ARTICLES_COLUMN_IS_PUBLISHED = "PUBLISHED";
    public static final String ARTICLES_COLUMN_IS_MYOWN = "OWN";
    public static final String ARTICLES_COLUMN_GROUP_ID = "GROUP_ID";
    public static final String ARTICLES_COLUMN_IMAGE_URL = "IMAGE_URL";
    public static final String ARTICLES_COLUMN_UPDATED_AT = "UPDATED";
    public static final String ARTICLES_COLUMN_CREATED_AT = "CREATED";

    private static final String CREATE_TABLE_ARTICLE = "" +
            "CREATE TABLE " + TABLE_ARTICLE + "(" +
            COLUMN_ID + " integer primary key ," +
            ARTICLES_COLUMN_TITLE   + " text," +
            ARTICLES_COLUMN_DESC  + " text," +
            ARTICLES_COLUMN_IS_PUBLISHED  + " integer," +
            ARTICLES_COLUMN_IS_MYOWN  + " integer," +
            ARTICLES_COLUMN_GROUP_ID  + " integer," +
            ARTICLES_COLUMN_IMAGE_URL  + " text," +
            ARTICLES_COLUMN_UPDATED_AT  + " integer," +
            ARTICLES_COLUMN_CREATED_AT  + " integer" +
            ");";

    public static final String TABLE_GROUP = "ARTICLE_GROUP";
    public static final String GROUPS_COLUMN_TITLE = "TITLE";

    private static final String CREATE_TABLE_GROUP = "" +
            "CREATE TABLE " + TABLE_GROUP + "(" +
            COLUMN_ID + " integer primary key ," +
            GROUPS_COLUMN_TITLE   + " text" +
            ");";

    public AppSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] CREATES = {
                CREATE_TABLE_ARTICLE,
                CREATE_TABLE_GROUP,
        };
        for (final String table : CREATES) {
            db.execSQL(table);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] TABLES = {
                TABLE_ARTICLE,
                TABLE_GROUP,
        };
        for (final String table : TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
    }
}
