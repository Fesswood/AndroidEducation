package com.vk.fesswod.articleView.data;

import android.content.ContentValues;
import android.database.Cursor;
import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.*;
/**
 * Created by sergeyb on 16.06.15.
 */
public class Article {
    long id;
    String title;
    String desc;
    boolean isPublished;
    boolean isMyOwn;
    ArticleGroup mArticleGroup;
    String mImageUri;
    long createAtTimeStamp;
    long updateAtTimeStamp;

    public Article(String title, String desc, boolean isMyOwn, int articleGroupId, long createAtTimeStamp) {
        this(-1, title, desc, false,isMyOwn,"", articleGroupId, createAtTimeStamp, -1l);

    }

    private Article(long id, String title, String desc, boolean isPublished,boolean isMyOwn,String ImageUrl, int articleGroupId, long createAtTimeStamp, long updateAtTimeStamp) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.isPublished = isPublished;
        this.isMyOwn = isMyOwn;
        this.mArticleGroup = Article.getCurrentGroup(articleGroupId);
        this.createAtTimeStamp = createAtTimeStamp;
        this.updateAtTimeStamp = updateAtTimeStamp;
    }

    private static ArticleGroup getCurrentGroup(int articleGroupId) {
        return null;
    }


    public ContentValues buildContentValues() {
        ContentValues cv = new ContentValues();
        if (id >= 0) {
            cv.put(COLUMN_ID, id);
        }
        cv.put(ARTICLES_COLUMN_TITLE, title);
        cv.put(ARTICLES_COLUMN_DESC, desc);
        cv.put(ARTICLES_COLUMN_IS_PUBLISHED, isPublished ? 1 : 0);
        cv.put(ARTICLES_COLUMN_IS_MYOWN, isMyOwn?1:0);
        cv.put(ARTICLES_COLUMN_GROUP_ID, mArticleGroup.getId());
        cv.put(ARTICLES_COLUMN_IMAGE_URL, mImageUri);
        cv.put(ARTICLES_COLUMN_UPDATED_AT, updateAtTimeStamp);
        cv.put(ARTICLES_COLUMN_CREATED_AT, createAtTimeStamp);
        return cv;
    }

    public static Article fromCursor(Cursor c){
        int idColId = c.getColumnIndex(COLUMN_ID);
        int titleColId = c.getColumnIndex(ARTICLES_COLUMN_TITLE);
        int descColId = c.getColumnIndex(ARTICLES_COLUMN_DESC);
        int publishColId = c.getColumnIndex(ARTICLES_COLUMN_IS_PUBLISHED);
        int owmColId = c.getColumnIndex(ARTICLES_COLUMN_IS_MYOWN);
        int groupIdColId = c.getColumnIndex(ARTICLES_COLUMN_GROUP_ID);
        int imageUrlolId = c.getColumnIndex(ARTICLES_COLUMN_IMAGE_URL);
        int updateColId = c.getColumnIndex(ARTICLES_COLUMN_UPDATED_AT);
        int createColId = c.getColumnIndex(ARTICLES_COLUMN_CREATED_AT);

        return new Article(
                c.getLong(idColId),
                c.getString(titleColId),
                c.getString(descColId),
                c.getInt(publishColId)==1?true:false,
                c.getInt(owmColId)==1?true:false,
                c.getString(imageUrlolId),
                c.getInt(groupIdColId),
                c.getLong(updateColId),
                c.getLong(createColId));
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) || (this.title == ((Article)o).getTitle() &&
                                   this.desc == ((Article)o).getDesc()   &&
                                   this.createAtTimeStamp == ((Article)o).getCreateAtTimeStamp() );
    }

    @Override
    public String toString() {
        return super.toString()+" title:"+title+
                                " desc:"+desc+
                                " mArticleGroup:"+mArticleGroup.getTitle()+
                                "  isPublished:"+isPublished;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    public ArticleGroup getArticleGroup() {
        return mArticleGroup;
    }

    public void setArticleGroup(ArticleGroup articleGroup) {
        this.mArticleGroup = articleGroup;
    }

    public long getCreateAtTimeStamp() {
        return createAtTimeStamp;
    }

    public void setCreateAtTimeStamp(long createAtTimeStamp) {
        this.createAtTimeStamp = createAtTimeStamp;
    }

    public long getUpdateAtTimeStamp() {
        return updateAtTimeStamp;
    }

    public void setUpdateAtTimeStamp(long updateAtTimeStamp) {
        this.updateAtTimeStamp = updateAtTimeStamp;
    }

    public boolean isMyOwn() {
        return isMyOwn;
    }

    public void setIsMyOwn(boolean isMyOwn) {
        this.isMyOwn = isMyOwn;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String imageUri) {
        mImageUri = imageUri;
    }
}
