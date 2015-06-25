package com.vk.fesswod.articleView.api;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.vk.fesswod.articleView.api.response.DataResponse;
import com.vk.fesswod.articleView.api.request.DataRequest;

import java.io.Serializable;

public class ApiService extends IntentService {

    private static final String DEBUG_TAG = ApiService.class.getSimpleName();

    public static final String CALLBACK_KEY         = "CALLBACK_KEY";
    public static final String ACTION_KEY           = "ACTION_KEY";
    public static final String ERROR_KEY            = "ERROR_KEY";
    public static final String REQUEST_OBJECT_KEY   = "REQUEST_OBJECT_KEY";
    public static final String RESPONSE_OBJECT_KEY  = "RESPONSE_OBJECT_KEY";

    public static final int ACTION_GET_CATEGORIES	= 0;
    public static final int ACTION_GET_ARTICLES		= 1;
    public static final int ACTION_POST_ARTICLE	    = 2;
    public static final int ACTION_EDIT_ARTICLE		= 3;
    public static final int ACTION_DELETE_ARTICLE	= 4;


    private boolean mDestroyed;
    private ResultReceiver mReceiver;

    public ApiService() {
        super(DEBUG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mReceiver = intent.getParcelableExtra(CALLBACK_KEY);
        int action = intent.getIntExtra(ACTION_KEY, -1);
        Bundle data = processIntent(intent, action);
        sentMessage(action, data);
    }

    private Bundle processIntent(Intent intent, int  action){

        DataResponse response	= null;
        Requester requester		= new Requester();
        Bundle bundle			= new Bundle();


        switch (action){
            case ACTION_GET_CATEGORIES:
                response = requester.getCategories((DataRequest) getRequestObject(intent));
                break;

            case ACTION_GET_ARTICLES:
                response = requester.getArticles((DataRequest) getRequestObject(intent));
                break;

            case ACTION_POST_ARTICLE:
                response = requester.addArticle((DataRequest) getRequestObject(intent));
                break;

            case ACTION_EDIT_ARTICLE:
                response = requester.editArticle((DataRequest) getRequestObject(intent));
                break;

            case ACTION_DELETE_ARTICLE:
                response = requester.deleteArticle((DataRequest) getRequestObject(intent));
                break;

            default:
                return null;
        }
        if(response == null){
            bundle.putBoolean(ERROR_KEY, true);
        } else {
            bundle.putSerializable(RESPONSE_OBJECT_KEY, response);
        }
        return bundle;
    }

    private Serializable getRequestObject(Intent intent){
        return intent.getSerializableExtra(REQUEST_OBJECT_KEY);
    }

    private void sentMessage(int code, Bundle data){
        if(!mDestroyed && mReceiver != null){
            mReceiver.send(code, data);
        }
    }

    @Override
    public void onCreate() {
        mDestroyed = false;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "onDestroy");
        mDestroyed = true;
        mReceiver = null;
        super.onDestroy();
    }
}
