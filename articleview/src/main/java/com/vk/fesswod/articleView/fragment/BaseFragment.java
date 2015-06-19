package com.vk.fesswod.articleView.fragment;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.fesswod.articleView.AppController;
import com.vk.fesswod.articleView.R;
import com.vk.fesswod.articleView.data.AppContentProvider;
import com.vk.fesswod.articleView.data.Article;
import com.vk.fesswod.articleView.data.ArticleGroup;
import com.vk.fesswod.articleView.rest.PhotoMultipartRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sergeyb on 16.06.15.
 */
public abstract class BaseFragment extends Fragment {


    private static final String DEBUG_TAG = BaseFragment.class.getSimpleName();
    private static final long MAX_FILE_SIZE = 99999999999999l;

    /**
     *
     * @param categoriesResponse
     */
    void receiveGroupsCallback(ArticleGroup.GroupContainer categoriesResponse){
        Log.d(DEBUG_TAG,"receiveGroupsCallback is empty!");
    }
    /**
     *
     * @param article
     */
    void receiveArticleCallback(Article article){
        Log.d(DEBUG_TAG,"receiveArticleCallback is empty!");
    }
    /**
     *
     * @param articleContainer
     */
    void receiveArticlesCallback(Article.ArticleContainer articleContainer){
        Log.d(DEBUG_TAG,"receiveArticlesCallback is empty!");
    }
    /**
     *
     * @param id
     */
    void receiveDeleteCallback(long id) {
        Log.d(DEBUG_TAG,"receiveDeleteCallback is empty!");
    }


    protected void sendRequestGetArticles()  {
        StringRequest req = new StringRequest(AppContentProvider.BASE_URL+"articles.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final Gson gson =  new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Article.class, new AppController.ArticleDeserializer())
                        .create();
                Article.ArticleContainer articleContainer = gson.fromJson(response,  Article.ArticleContainer.class);
                receiveArticlesCallback(articleContainer);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                showSnackbar(R.string.snackbar_article_text, R.string.snackbar_action, new
                View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        sendRequestGetArticles();
                    }
                });
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","Token token="+ AppContentProvider.TOKEN);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }



    protected abstract void showSnackbar(int stringResource, int scnakbarActionString, View.OnClickListener listener);


    protected void sendRequestGetGroups() {
        StringRequest req = new StringRequest(AppContentProvider.BASE_URL+"categories.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ArticleGroup.GroupContainer categoriesResponse = gson.fromJson(response, ArticleGroup.GroupContainer.class);
                receiveGroupsCallback(categoriesResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                showSnackbar(R.string.snackbar_group_text, R.string.snackbar_action, new
                        View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sendRequestGetGroups();
                            }
                        });
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","Token token="+ AppContentProvider.TOKEN);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }

    protected void sendRequestSaveArticle(final Article article) throws JSONException {

        final Gson gson =  new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Article.class, new AppController.ArticlefSerializer())
                .create();
        String jsonBody =  gson.toJson(article);
        JSONObject jsonObject = new JSONObject(jsonBody);
        JsonObjectRequest authorizationRequest = new JsonObjectRequest(Request.Method.POST,AppContentProvider.BASE_URL + "articles.json",jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final Gson gson =  new GsonBuilder()
                                .setPrettyPrinting()
                                .registerTypeAdapter(Article.class, new AppController.ArticleDeserializer())
                                .create();
                        String s = response.toString();
                        Article articleResived= gson.fromJson(s,Article.class);
                        receiveArticleCallback(articleResived);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                showSnackbar(R.string.snackbar_article_save_text, R.string.snackbar_action, new
                        View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                try {
                                    sendRequestSaveArticle(article);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token token=" + AppContentProvider.TOKEN);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(authorizationRequest);
    }


    protected void sendRequestUpdateArticle(final Article article) throws JSONException {

        final Gson gson =  new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Article.class, new AppController.ArticlefSerializer())
                .create();
        String jsonBody =  gson.toJson(article);
        JSONObject jsonObject = new JSONObject(jsonBody);
        JsonObjectRequest authorizationRequest = new JsonObjectRequest(Request.Method.PUT,AppContentProvider.BASE_URL + "articles/"+article.getId()+".json",jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final Gson gson =  new GsonBuilder()
                                .setPrettyPrinting()
                                .registerTypeAdapter(Article.class, new AppController.ArticleDeserializer())
                                .create();
                        String s = response.toString();
                        Article articleResived= gson.fromJson(s,Article.class);
                        receiveArticleCallback(articleResived);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                showSnackbar(R.string.snackbar_article_save_text, R.string.snackbar_action, new
                        View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                try {
                                    sendRequestSaveArticle(article);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token token=" + AppContentProvider.TOKEN);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(authorizationRequest);
    }

    protected void sendRequestDeleteArticle(final long id) {

        StringRequest req = new StringRequest(Request.Method.DELETE, AppContentProvider.BASE_URL+"/articles/"+id+".json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                receiveDeleteCallback(id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                showSnackbar(R.string.snackbar_delete_text, R.string.snackbar_action,null);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization","Token token="+ AppContentProvider.TOKEN);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }
    protected  void sendRequestPostPhoto(long articleID,Uri photoUri){
       PhotoMultipartRequest req= new PhotoMultipartRequest(
                AppContentProvider.BASE_URL + "articles/" + articleID + "/photos.json"
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(DEBUG_TAG,"Error "+ error.getMessage());
            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.d(DEBUG_TAG, "Ehuuuuuuu ");
            }
        },new File(getPath(getActivity(),photoUri))){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();


                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Token token=" + AppContentProvider.TOKEN);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
