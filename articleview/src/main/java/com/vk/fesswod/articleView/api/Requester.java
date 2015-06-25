package com.vk.fesswod.articleView.api;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.fesswod.articleView.AppController;
import com.vk.fesswod.articleView.api.request.RequestImageUrlWrapper;
import com.vk.fesswod.articleView.api.response.ResponseArticleWrapper;
import com.vk.fesswod.articleView.api.response.ResponseArticlesWrapper;
import com.vk.fesswod.articleView.api.response.ResponseCategoriesWrapper;
import com.vk.fesswod.articleView.api.response.DataResponse;
import com.vk.fesswod.articleView.api.request.DataRequest;
import com.vk.fesswod.articleView.api.response.ResponseImageUrlWrapper;
import com.vk.fesswod.articleView.api.utils.Utils;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.AppSQLiteOpenHelper;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleCategory;

import java.io.File;
import java.util.ArrayList;

public class Requester {
    private static final String DEBUG_TAG = Requester.class.getSimpleName();
    private static final String BASE_URL = "http://editors.yozhik.sibext.ru/";

    public Requester() {
    }

    public DataResponse getCategories(DataRequest request) {
        RestClient  restClient = new RestClient();
        String      url        = getCategoriesUrl();
        ApiResponse response   = restClient.doGet(url);
        Gson        gson       = new Gson();

        ResponseCategoriesWrapper categoriesResponse = deserialize(gson
                , response
                , ResponseCategoriesWrapper.class);

        if(categoriesResponse != null){
            for (ArticleCategory category: categoriesResponse.categories){
                AppController.getAppContext().getContentResolver()
                        .insert(AppContentProvider.CONTENT_URI_GROUPS, category.buildContentValues());
            }
        }
        return new DataResponse();
    }


    public DataResponse getArticles(DataRequest request) {

        //TODO:start volley
        RestClient restClient = new RestClient();
        String url = getArticlesUrl();
        ApiResponse response = restClient.doGet(url);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create();
        ResponseArticlesWrapper articlesWrapper = deserialize(gson, response, ResponseArticlesWrapper.class);

        // store to base
        if(articlesWrapper != null && articlesWrapper.getArticles() != null){
            ArrayList<Long> ids = new ArrayList<>();
            // delete articles, that not exists in server's articles list
            Cursor cursor	= AppController.getAppContext().getContentResolver()
                    .query(AppContentProvider.CONTENT_URI_ARTICLES
                            , new String[]{AppSQLiteOpenHelper.COLUMN_ID}
                            , null
                            , null
                            , null);
            while (cursor.moveToNext()){
                ids.add(cursor.getLong( cursor.getColumnIndex(AppSQLiteOpenHelper.COLUMN_ID)) );
            }

            cursor.close();

            for (Article article: articlesWrapper.getArticles()){
                ids.remove(article.getId());

                AppController.getAppContext().getContentResolver()
                        .insert(AppContentProvider.CONTENT_URI_ARTICLES, article.buildContentValues());
            }

            // delete articles, than not exists in server
            for(long id: ids){
                AppController.getAppContext().getContentResolver()
                        .delete(Uri.withAppendedPath(AppContentProvider.CONTENT_URI_ARTICLES,""+id), null, null);
            }
        }
        return new DataResponse();
    }

    public DataResponse addArticle(DataRequest request) {
        long id	= -1;

        RestClient restClient	= new RestClient();
        String url				= putArticleUrl();
        Gson gson				= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create();
        ApiResponse response	= restClient.doPost(url, null, gson.toJson(request.getArticle()));
        ResponseArticleWrapper responseWrapper = deserialize(gson, response, ResponseArticleWrapper.class);

        if(responseWrapper != null && responseWrapper.getArticle() != null){
            //insert into db
            id	= responseWrapper.getArticle().getId();
            AppController.getAppContext().getContentResolver()
                    .insert(AppContentProvider.CONTENT_URI_ARTICLES
                            , responseWrapper.getArticle().buildContentValues());
            RequestImageUrlWrapper photoContainer = request.getArticle().getPhotoContainer();
            if( photoContainer != null &&
                    !TextUtils.isEmpty(photoContainer.getImageUrl())){
                addPhotoToArticle(id, photoContainer.getImageUrl());
            }
            Log.d(DEBUG_TAG, "article successfully sent");
        }

        return new DataResponse(id);
    }

    public DataResponse deleteArticle(DataRequest request) {
        long id	= -1;
        RestClient restClient	= new RestClient();
        String url				= deleteArticleUrl(request.getId());

        ApiResponse response	= restClient.doDelete(url);

        if(response.status == 200){
            //delete in db
            id	= request.getId();
            AppController.getAppContext().getContentResolver()
                    .delete(Uri.withAppendedPath(AppContentProvider.CONTENT_URI_ARTICLES, "" + id),
                            null, null);
            Log.d(DEBUG_TAG, "article succesful edited");
        }

        return new DataResponse(id);
    }

    public DataResponse editArticle(DataRequest request) {
        long id	= -1;
        Log.d(DEBUG_TAG, "editArticle has started");

        RestClient restClient	= new RestClient();
        String url				= editArticleUrl(request.getArticle().getId());
        Gson gson				= new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create();

        ApiResponse response	= restClient.doPut(url, gson.toJson(request.getArticle()));
        ResponseArticleWrapper responseContainer = deserialize(gson, response, ResponseArticleWrapper.class);

        if(responseContainer != null && responseContainer.getArticle() != null){

            id	= responseContainer.getArticle().getId();
            // replace old row in database
          AppController.getAppContext().getContentResolver()
                  .update(Uri.withAppendedPath(AppContentProvider.CONTENT_URI_ARTICLES, "" + id),
                          responseContainer.getArticle().buildContentValues(), null, null);

            RequestImageUrlWrapper photoContainer = request.getArticle().getPhotoContainer();
            if( photoContainer != null &&
                    !TextUtils.isEmpty(photoContainer.getImageUrl())){
                addPhotoToArticle(id, photoContainer.getImageUrl());
            }
            Log.d(DEBUG_TAG, "article has been changed successfully");
        }

        return new DataResponse(id);
    }

    private void addPhotoToArticle(long id, String imagePath) {
        RestClient restClient				= new RestClient();
        String url							= addImageUrl(id);
        Gson gson							= new Gson();
        File file							= new File(imagePath);
        String response						= restClient.doMultiPartRequest(url, file, "photo[image]");
        ResponseImageUrlWrapper responseContainer	= deserialize(gson, response, ResponseImageUrlWrapper.class);

        if(responseContainer != null && responseContainer.getPhoto() != null) {
            //update in db
            ContentValues values	= new ContentValues();
            values.put(AppSQLiteOpenHelper.ARTICLES_COLUMN_IMAGE_URL,responseContainer.getPhoto().getImageUrl());
            AppController.getAppContext().getContentResolver()
                    .update(Uri.withAppendedPath(AppContentProvider.CONTENT_URI_ARTICLES, "" + id),
                            values, null, null);
            Log.d(DEBUG_TAG, "photo has  ");
        }
    }


    private String getCategoriesUrl(){
        return BASE_URL.concat("categories.json");
    }

    private String getArticlesUrl(){
        return BASE_URL.concat("articles.json");
    }

    private String putArticleUrl(){
        return BASE_URL.concat("articles.json");
    }

    private String editArticleUrl(long id){
        return BASE_URL.concat(String.format("articles/%d.json", id));
    }

    private String deleteArticleUrl(long id){
        return BASE_URL.concat(String.format("articles/%d.json",id));
    }

    private String addImageUrl(long id){
        return BASE_URL.concat(String.format("articles/%d/photos.json", id));

    }

    private  <T> T deserialize(Gson gson, ApiResponse response, Class<T> classOfT){
        if(response != null){
            try {
                return gson.fromJson(response.getInputStreamReader()
                        , classOfT);
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private  <T> T deserialize(Gson gson, String response, Class<T> classOfT){
        if(response != null){
            try {
                return gson.fromJson(response, classOfT);
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
