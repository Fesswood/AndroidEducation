package com.vk.fesswod.articleView;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleGroup;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sergeyb on 18.06.15.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();


    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static class ArticlefSerializer implements JsonSerializer<Article>
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");



            if(jsonObject.get("created_at") != null){
                try {
                    Date created_at = simpleDateFormat.parse(jsonObject.get("created_at").getAsString());
                    article.setCreateAtTimeStamp(created_at.getTime()/1000l);
                } catch (ParseException e) {
                    e.printStackTrace();
                    article.setCreateAtTimeStamp(0);
                }
            }
            if(jsonObject.get("created_at") != null){
                try {
                    Date created_at = simpleDateFormat.parse(jsonObject.get("updated_at").getAsString());
                    article.setUpdateAtTimeStamp(created_at.getTime()/1000l);
                } catch (ParseException e) {
                    e.printStackTrace();
                    article.setCreateAtTimeStamp(0);
                }
            }
            JsonElement photo = jsonObject.get("photo");
            if(photo!=null){
                article.setImageUri(photo.getAsJsonObject().get("url").getAsString());
            }

            return article;
        }
    }
    public static class ArticleGroupDeserializer implements JsonDeserializer<ArticleGroup>
    {
        @Override
        public ArticleGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            ArticleGroup articleGroup;
            JsonObject jsonObject = json.getAsJsonObject();
            articleGroup = new ArticleGroup(   jsonObject.get("id").getAsLong(),
                    jsonObject.get("title").getAsString()
            );

          /* jsonObject.get("published").getAsBoolean();
            jsonObject.get("category_id").getAsInt();
            jsonObject.get("created_at").getAsString();
            jsonObject.get("updated_at").getAsString();*/

            return articleGroup;
        }
    }

}