package com.vk.fesswod.articleView.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.*;
/**
 * Created by sergeyb on 16.06.15.
 */
public class Article {
    long id;
    private long server_id;
    String title;
    String desc;
    boolean isPublished;
    boolean isMyOwn;
    long mArticleGroupId;
    String mImageUri;
    long createAtTimeStamp;
    long updateAtTimeStamp;

    public Article(String title, String desc){
        this(title,desc,true,-1,System.currentTimeMillis()/1000l);
    }

    public Article(String title, String desc, boolean isMyOwn, int articleGroupId, long createAtTimeStamp) {
        this(-1, title, desc, false,isMyOwn,"", articleGroupId, createAtTimeStamp, -1l);

    }

    private Article(long id, String title, String desc, boolean isPublished,boolean isMyOwn,String ImageUrl, int articleGroupId, long createAtTimeStamp, long updateAtTimeStamp) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.isPublished = isPublished;
        this.isMyOwn = isMyOwn;
        this.mImageUri=ImageUrl;
        this.mArticleGroupId = articleGroupId;
        this.createAtTimeStamp = createAtTimeStamp;
        this.updateAtTimeStamp = updateAtTimeStamp;
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
        cv.put(ARTICLES_COLUMN_GROUP_ID, mArticleGroupId);
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
        int imageUrlId = c.getColumnIndex(ARTICLES_COLUMN_IMAGE_URL);
        int updateColId = c.getColumnIndex(ARTICLES_COLUMN_UPDATED_AT);
        int createColId = c.getColumnIndex(ARTICLES_COLUMN_CREATED_AT);

        return new Article(
                c.getLong(idColId),
                c.getString(titleColId),
                c.getString(descColId),
                c.getInt(publishColId)==1?true:false,
                c.getInt(owmColId)==1?true:false,
                c.getString(imageUrlId),
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
                                " mArticleGroupId:"+ mArticleGroupId+
                                "  isPublished:"+isPublished;
    }


    public static class ArticleSerializer implements JsonSerializer<Article>
    {
        @Override
        public JsonElement serialize(Article src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject result = new JsonObject();
            JsonObject fields = new JsonObject();
            fields.addProperty("title", src.getTitle());
            fields.addProperty("description", src.getDesc());
            fields.addProperty("published", src.isPublished());
            fields.addProperty("category_id", src.getArticleGroupId());

            result.add("Article", fields);
            return fields;
        }
    }


    public static class ArticleDeserializer implements JsonDeserializer<Article>
    {
        private static final String DEBUG_TAG = ArticleDeserializer.class.getSimpleName();

        @Override
        public Article deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            Article article;
            JsonObject jsonObject;
            JsonElement jsonRoot = json.getAsJsonObject().get("article");
            if(jsonRoot != null){
                jsonObject = jsonRoot.getAsJsonObject();
            }else{
                jsonObject = json.getAsJsonObject();
            }


            article = new Article(jsonObject.get("title").getAsString(),
                                  jsonObject.get("description").getAsString()
            );
            article.setId(jsonObject.get("id").getAsLong());
            article.setIsMyOwn(jsonObject.get("own").getAsBoolean());
            article.setArticleGroupId(jsonObject.get("category_id").getAsInt());

            if(jsonObject.get("photo") != null){
                article.setImageUri(jsonObject.get("photo").getAsJsonObject().get("url").getAsString());
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            if(jsonObject.get("created_at") != null){
                try {
                    Date created_at = simpleDateFormat.parse(jsonObject.get("created_at").getAsString());
                    article.setCreateAtTimeStamp(created_at.getTime()/1000l);
                } catch (ParseException e) {
                    Log.d(DEBUG_TAG,e.getMessage());
                    article.setCreateAtTimeStamp(0);
                }
            }
            if(jsonObject.get("created_at") != null){
                try {
                    Date created_at = simpleDateFormat.parse(jsonObject.get("updated_at").getAsString());
                    article.setUpdateAtTimeStamp(created_at.getTime()/1000l);
                } catch (ParseException e) {
                    Log.d(DEBUG_TAG, e.getMessage());
                    article.setCreateAtTimeStamp(0);
                }
            }

            return article;
        }
    }



    public static class ArticleContainer{
        public Article[] articles;

        public ArticleContainer(Article[] articles) {
            this.articles = articles;
        }

        public ArticleContainer() {

        }
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

    public long getArticleGroupId() {
        return mArticleGroupId;
    }

    public void setArticleGroupId(long articleGroupId) {
        this.mArticleGroupId = articleGroupId;
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

    public long getServer_id() {
        return server_id;
    }

    public void setServer_id(long server_id) {
        this.server_id = server_id;
    }
}
