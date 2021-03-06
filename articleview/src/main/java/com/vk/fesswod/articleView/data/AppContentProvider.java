package com.vk.fesswod.articleView.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.*;
import java.util.Arrays;


public class AppContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.app.content_provider";
    private static final String PATH_ARTICLES = AppSQLiteOpenHelper.TABLE_ARTICLE;
    private static final String PATH_GROUPS = AppSQLiteOpenHelper.TABLE_GROUP;
    private static final String PATH_NOT_EMPTY = "NOTEMPTYGROUPS";

    public static final Uri CONTENT_URI_ARTICLES = Uri.parse("content://" + AUTHORITY + "/" + PATH_ARTICLES);
    public static final Uri CONTENT_URI_GROUPS = Uri.parse("content://" + AUTHORITY + "/" + PATH_GROUPS);
    public static final Uri CONTENT_URI_PATH_NOT_EMPTY = Uri.parse("content://" + AUTHORITY + "/" + PATH_NOT_EMPTY);
    private static final int CODE_ARTICLES = 0;
    private static final int CODE_ARTICLE = 2;
    private static final int CODE_GROUPS = 1;
    private static final int CODE_NOT_EMPTY_GROUPS = 3;


    private static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        sURI_MATCHER.addURI(AUTHORITY, PATH_ARTICLES, CODE_ARTICLES);
        sURI_MATCHER.addURI(AUTHORITY, PATH_ARTICLES + "/#", CODE_ARTICLE);
        sURI_MATCHER.addURI(AUTHORITY, PATH_GROUPS, CODE_GROUPS);
        sURI_MATCHER.addURI(AUTHORITY, PATH_NOT_EMPTY, CODE_NOT_EMPTY_GROUPS);
    }

    private static AppSQLiteOpenHelper dbHelper;

    public synchronized static AppSQLiteOpenHelper getDbHelper(Context context) {
        if (null == dbHelper) {
            dbHelper = new AppSQLiteOpenHelper(context);
        }
        return dbHelper;
    }

    @Override
    public boolean onCreate() {
        getDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sURI_MATCHER.match(uri);
        switch (match) {
            case CODE_GROUPS:
                return getGroups();
            case CODE_NOT_EMPTY_GROUPS:
                return getNotEmptyGroups();
        }
        String table = parseUri(uri);
        Cursor cursor = dbHelper.getReadableDatabase()
                .query(table, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = parseUri(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        boolean replace = false;
        if ( values.containsKey(SQL_INSERT_OR_REPLACE)) {
            replace = values.getAsBoolean(SQL_INSERT_OR_REPLACE);

            // Clone the values object, so we don't modify the original.
            // This is not strictly necessary, but depends on your needs
            values = new ContentValues(values);

            // Remove the key, so we don't pass that on to db.insert() or db.replace()
            values.remove(SQL_INSERT_OR_REPLACE);
        }

        long id = -1;
        Uri resultUri;
        if ( replace ) {
            id = db.replace(tableName, null, values);
        } else {
            id = db.insert(tableName, null, values);

        }

        if (-1 == id) {
            throw new RuntimeException("Record wasn't saved.");
        }
        resultUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(resultUri, null);

        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = sURI_MATCHER.match(uri);
        int rowsDeleted=0;
        String table = parseUri(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (code) {
            case CODE_GROUPS:
                rowsDeleted = db.delete(table, selection,
                        selectionArgs);
                break;
            case CODE_ARTICLES:
                rowsDeleted = db.delete(table, selection,
                        selectionArgs);
                break;
            case CODE_ARTICLE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(table,
                            AppSQLiteOpenHelper.COLUMN_ID + "= ?",
                            new String[]{id});
                } else {
                    rowsDeleted = db.delete(table,
                            AppSQLiteOpenHelper.COLUMN_ID + "= ? "
                                    + " and " + selection,
                            combine( new String[]{id} , selectionArgs));
                }
                break;
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = 0;
        int uriId = sURI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if ( values.containsKey(SQL_INSERT_OR_REPLACE)) {
            // Clone the values object, so we don't modify the original.
            // This is not strictly necessary, but depends on your needs
            values = new ContentValues(values);

            // Remove the key, so we don't pass that on to db.insert() or db.replace()
            values.remove(SQL_INSERT_OR_REPLACE);
        }


        switch (uriId){

            case CODE_ARTICLES:
                // update all articles
                result	= db.update(AppSQLiteOpenHelper.TABLE_ARTICLE, values, selection, selectionArgs);
                break;

            case CODE_ARTICLE:
                // update by ID
                String id	= uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    result = db.update(AppSQLiteOpenHelper.TABLE_ARTICLE
                            , values, AppSQLiteOpenHelper.COLUMN_ID + " = ?", new String[]{id});
                } else {
                    result = db.update(AppSQLiteOpenHelper.TABLE_ARTICLE
                            , values, AppSQLiteOpenHelper.COLUMN_ID + " = " + id + " AND "
                            + selection, selectionArgs);
                }
                break;

            case CODE_GROUPS:
                result	= db.update(AppSQLiteOpenHelper.TABLE_GROUP, values, selection,selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    private String parseUri(Uri uri) {
        return parseUri(sURI_MATCHER.match(uri));
    }

    private String parseUri(int match) {
        String table = null;
        switch (match) {
            case CODE_ARTICLES:
                table = AppSQLiteOpenHelper.TABLE_ARTICLE;
                break;
            case CODE_GROUPS:
                table = AppSQLiteOpenHelper.TABLE_GROUP;
                break;
            case CODE_ARTICLE:
                table = AppSQLiteOpenHelper.TABLE_ARTICLE;
                break;
            case CODE_NOT_EMPTY_GROUPS:
                table = AppSQLiteOpenHelper.TABLE_GROUP;
                break;
            default:
                throw new IllegalArgumentException("Invalid code: " + match);
        }
        return table;
    }

    /*
    custom sql queries
     */

    private Cursor getGroups() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from "+ AppSQLiteOpenHelper.TABLE_GROUP;
        return db.rawQuery(sql, null);
    }
    private Cursor getNotEmptyGroups() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = " select "+TABLE_GROUP+"."+COLUMN_ID+", "+
                    TABLE_GROUP+"."+GROUPS_COLUMN_TITLE+
                    " from "+ TABLE_GROUP
                    +" inner join " +
                    TABLE_ARTICLE
                    +" on "+ TABLE_GROUP+"."+COLUMN_ID + " = " + TABLE_ARTICLE+"."+ARTICLES_COLUMN_GROUP_ID
                    +" group by "  + TABLE_ARTICLE+"."+ARTICLES_COLUMN_GROUP_ID;
        return db.rawQuery(sql, null);
    }


    public static String[] combine(String[] a, String[] b) {
        int length = a.length + b.length;
        String[] result = new String[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}
