package com.vk.fesswod.articleView;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.vk.fesswod.articleView.data.ArticleCategory;

import java.lang.reflect.Type;

/**
 * Created by sergeyb on 18.06.15.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();


    private RequestQueue mRequestQueue;

    private static Context appContext;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appContext = getApplicationContext();
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


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }



    public static Context getAppContext(){
        return appContext;
    }

}