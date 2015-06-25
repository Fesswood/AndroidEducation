package com.vk.fesswod.articleView.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.vk.fesswod.articleView.api.request.RequestImageUrlWrapper;
import static com.vk.fesswod.articleView.data.AppSQLiteOpenHelper.*;
/**
 * Created by sergeyb on 16.06.15.
 */
public class Article  implements Serializable{

    private long id;

    @Expose
    private String title;

    @Expose
    @SerializedName("description")
    private String desc;

    @Expose
    @SerializedName("published")
    private boolean isPublished=true;

    @SerializedName("own")
    private boolean isMyOwn;

    @Expose
    @SerializedName("category_id")
    private long mArticleGroupId;

    @SerializedName("photo")
    private RequestImageUrlWrapper mPhotoContainer = new RequestImageUrlWrapper();

    @SerializedName("created_at")
    private Date createAtTime;

    @SerializedName("updated_at")
    private Date updateAtTimeStamp;


    public Article(String title, String desc){
        this(title,desc,true,-1, new Date());
    }

    public Article(String title, String desc, boolean isMyOwn, int articleGroupId, Date createAtTime) {
        this(-1, title, desc, false, isMyOwn, null, articleGroupId, createAtTime, createAtTime);

    }

    private Article(long id, String title, String desc, boolean isPublished,boolean isMyOwn,RequestImageUrlWrapper ImageUrl, int articleGroupId, Date createAtTime, Date updateAtTimeStamp) {
        this.id = id;
        this.title = (title);
        this.desc = (desc);
        this.isPublished = (isPublished);
        this.isMyOwn = (isMyOwn);
        this.mPhotoContainer = ImageUrl;
        this.setArticleGroupId(articleGroupId);
        this.createAtTime = (createAtTime);
        this.updateAtTimeStamp = (updateAtTimeStamp);
    }

    public ContentValues buildContentValues() {
        ContentValues cv = new ContentValues();
        if (id >= 0) {
            cv.put(COLUMN_ID, id);
        }
        cv.put(SQL_INSERT_OR_REPLACE,true);
        cv.put(ARTICLES_COLUMN_TITLE, title);
        cv.put(ARTICLES_COLUMN_DESC, desc);
        cv.put(ARTICLES_COLUMN_IS_PUBLISHED, isPublished ? 1 : 0);
        cv.put(ARTICLES_COLUMN_IS_MYOWN, isMyOwn ?1:0);
        cv.put(ARTICLES_COLUMN_GROUP_ID, mArticleGroupId);
        if(getPhotoContainer() != null){
            cv.put(ARTICLES_COLUMN_IMAGE_URL, getPhotoContainer().getImageUrl());
        }else {
            cv.put(ARTICLES_COLUMN_IMAGE_URL, "");
        }
        cv.put(ARTICLES_COLUMN_UPDATED_AT, getUpdateAtTime().getTime());
        cv.put(ARTICLES_COLUMN_CREATED_AT, getCreateAtTime().getTime());
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
                c.getInt(publishColId)==1,
                c.getInt(owmColId)==1,
                new RequestImageUrlWrapper(c.getString(imageUrlId)),
                c.getInt(groupIdColId),
                new Date(c.getLong(updateColId)),
                new Date(c.getLong(createColId)));
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) || (this.getTitle().equals(((Article) o).getTitle()) &&
                                   this.getDesc().equals(((Article) o).getDesc())   &&
                                   this.getCreateAtTime() == ((Article)o).getCreateAtTime() );
    }

    @Override
    public String toString() {
        return super.toString()+" title:"+ getTitle() +
                                " desc:"+ getDesc() +
                                " mArticleGroupId: "+ getArticleGroupId() +
                                " isPublished:"+ isPublished();
    }

    public RequestImageUrlWrapper getPhotoContainer() {
        return mPhotoContainer;
    }

    public void setPhotoContainer(RequestImageUrlWrapper photoContainer) {
        mPhotoContainer = photoContainer;
    }


   /* public static class ArticleSerializer implements JsonSerializer<Article>
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
    }*/


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
                    article.setCreateAtTime(created_at);
                } catch (ParseException e) {
                    Log.d(DEBUG_TAG,e.getMessage());
                    article.setCreateAtTime(new Date());
                }
            }
            if(jsonObject.get("created_at") != null){
                try {
                    Date created_at = simpleDateFormat.parse(jsonObject.get("updated_at").getAsString());
                    article.setUpdateAtTime(created_at);
                } catch (ParseException e) {
                    Log.d(DEBUG_TAG, e.getMessage());
                    article.setUpdateAtTime(new Date());
                }
            }

            return article;
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

    public boolean isMyOwn() {
        return isMyOwn;
    }

    public void setIsMyOwn(boolean isMyOwn) {
        this.isMyOwn = isMyOwn;
    }


    public void setImageUri(String imageUri) {
        if(mPhotoContainer == null){
            mPhotoContainer = new RequestImageUrlWrapper();
        }
        mPhotoContainer.setImageUrl(imageUri);
    }

    public Date getCreateAtTime() {
        return createAtTime;
    }

    public void setCreateAtTime(Date createAtTime) {
        this.createAtTime = createAtTime;
    }

    public Date getUpdateAtTime() {
        return updateAtTimeStamp;
    }

    public void setUpdateAtTime(Date updateAtTimeStamp) {
        this.updateAtTimeStamp = updateAtTimeStamp;
    }

}
