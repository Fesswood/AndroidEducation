package com.vk.fesswod.articleView.api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.vk.fesswod.articleView.AppController;
import com.vk.fesswod.articleView.api.request.DataRequest;

import java.io.Serializable;

import static com.vk.fesswod.articleView.api.ApiService.*;

public class ApiServiceHelper {
    private static final String DEBUG_TAG = ApiService.class.getSimpleName();


    private static ApiServiceHelper instance;

    public static ApiServiceHelper getInstance() {
        if (instance == null) {
            instance = new ApiServiceHelper();
        }
        return instance;
    }

    private ApiServiceHelper() {
        super();
        Log.d(DEBUG_TAG, "Constructor");
    }

    public void getCategories(Serializable data, ResultReceiver onServiceResult){
        Log.d(DEBUG_TAG, "Constructor");
        startService(data, ACTION_GET_CATEGORIES, onServiceResult);
    }

    public void getArticles(DataRequest data, ResultReceiver onServiceResult){
        startService(data, ACTION_GET_ARTICLES, onServiceResult);
    }

    public void addArticle(DataRequest data, ResultReceiver onServiceResult){
        startService(data, ACTION_POST_ARTICLE, onServiceResult);
    }

    public void deleteArticle(DataRequest data, ResultReceiver onServiceResult){
        startService(data, ACTION_DELETE_ARTICLE, onServiceResult);
    }

    public void editArticle(DataRequest data, ResultReceiver onServiceResult){
        startService(data, ACTION_EDIT_ARTICLE, onServiceResult);
    }


    private void startService(Serializable data, int action, ResultReceiver onServiceResult) {
        Log.d(DEBUG_TAG, "startService with tag "+action);
        Intent intent = getIntent(action, onServiceResult);
        if(data != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(REQUEST_VALUE_KEY, data);
            intent.putExtra(REQUEST_WRAPPER_KEY, bundle);
        }
        getContext().startService(intent);
    }

    private Intent getIntent(int action, ResultReceiver onServiceResult) {
        Log.d(DEBUG_TAG, "getIntent");
        final Intent i = new Intent(getContext(), ApiService.class);
        i.putExtra(ACTION_KEY, action);
        i.putExtra(CALLBACK_KEY, onServiceResult);
        return i;
    }

    private Context getContext(){
        return AppController.getAppContext();
    }
}
