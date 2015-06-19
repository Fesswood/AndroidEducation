package com.vk.fesswod.articleView.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.GROUPS_COLUMN_TITLE;
import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.COLUMN_ID;

/**
 * Created by sergeyb on 16.06.15.
 */
public class ArticleGroup {
    long id;
    String title;

    public ArticleGroup(String title) {
        this.title = title;
    }

    public ArticleGroup(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) || title.equals(((ArticleGroup)o).getTitle());
    }

    public ContentValues buildContentValues() {
        ContentValues cv = new ContentValues();
        if (id >= 0) {
            cv.put(COLUMN_ID, id);
        }
        cv.put(GROUPS_COLUMN_TITLE, title);
        return cv;
    }

    public static ArticleGroup fromCursor(Cursor c){
        int idColId = c.getColumnIndex(COLUMN_ID);
        int titleColId = c.getColumnIndex(GROUPS_COLUMN_TITLE);

        return new ArticleGroup(
                c.getLong(idColId),
                c.getString(titleColId));
    }
    public static ArrayList<ArticleGroup> createListFromCursor(Cursor c){
        ArrayList<ArticleGroup> list = new ArrayList<>();
        while (c.moveToNext()){
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public static class GroupContainer{
        public ArticleGroup[] categories;

        public GroupContainer(ArticleGroup[] categories) {
            this.categories = categories;
        }
    }

}
