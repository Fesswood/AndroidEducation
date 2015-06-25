package com.vk.fesswod.articleView.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.vk.fesswod.articleView.AppController;
import com.vk.fesswod.articleView.api.ApiService;
import com.vk.fesswod.articleView.api.ApiServiceHelper;
import com.vk.fesswod.articleView.api.request.DataRequest;
import com.vk.fesswod.articleView.api.response.DataResponse;
import com.vk.fesswod.articleView.api.response.ResponseArticleWrapper;
import com.vk.fesswod.articleView.api.response.ResponseArticlesWrapper;
import com.vk.fesswod.articleView.api.response.ResponseCategoriesWrapper;

/**
 * Created by sergeyb on 16.06.15.
 */
public abstract class BaseFragment extends Fragment {


    private static final String DEBUG_TAG = BaseFragment.class.getSimpleName();

    private static final int GET_ARTICLES = 0;
    private static final int ADD_ARTICLES = 1;
    private static final int EDIT_ARTICLES = 2;
    private static final int DELETE_ARTICLES = 3;


    protected abstract void showSnackbar(int stringResource, int scnakbarActionString, View.OnClickListener listener);

    protected interface HttpResponseErrorListener {
        void onError();
    }
    protected interface HttpResponseListener {
        void onResponse(long id, int operationId);
    }

    public void sendGetArticlesRequest(final HttpResponseListener responseListener
            , final HttpResponseErrorListener errorListener, int operationID){
        ApiServiceHelper.getInstance().getArticles(new DataRequest(), new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleReceivedResult(resultData, errorListener, responseListener, operationID);
            }
        });
    }

    public void sendAddArticleRequest(String body, String imagePath
            , final HttpResponseListener responseListener
            , final HttpResponseErrorListener errorListener
            , int operationID){

        ApiServiceHelper.getInstance().addArticle(new DataRequest(body, imagePath)
                , new ResultReceiver(new Handler()) {

            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleReceivedDataResult(resultData, errorListener, responseListener, operationID);
            }
        });
    }


    public void sendEditArticleRequest(long id, String body, String imagePath
            , final HttpResponseListener responseListener
            , final HttpResponseErrorListener errorListener
            , int operationID){
        ApiServiceHelper.getInstance().editArticle(new DataRequest(id
                , body, imagePath), new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleReceivedDataResult(resultData, errorListener, responseListener, operationID);
            }
        });
    }

    public void sendDeleteArticleRequest(long id
            , final HttpResponseListener responseListener
            , final HttpResponseErrorListener errorListener
            , int operationID){

        ApiServiceHelper.getInstance().deleteArticle(new DataRequest(id), new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleReceivedResult(resultData, errorListener, responseListener, operationID);
            }
        });
    }

    public void sendGetCategoriesRequest(final HttpResponseListener responseListener
            , final HttpResponseErrorListener errorListener){

        ApiServiceHelper.getInstance().getCategories(new DataRequest(), new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                handleReceivedResult(resultData, errorListener, responseListener, -1);
            }
        });
    }

    private void handleReceivedResult(Bundle resultData,
                                      HttpResponseErrorListener errorListener,
                                      HttpResponseListener responseListener, int operationID) {
        if (resultData.containsKey(ApiService.ERROR_KEY)) {
            if (errorListener != null) {
                errorListener.onError();
            }
        } else {
            if (responseListener != null) {
                responseListener.onResponse(0L,operationID);
            }
        }
    }

    private void handleReceivedDataResult(Bundle resultData,
                                          HttpResponseErrorListener errorListener,
                                          HttpResponseListener responseListener, int operationID) {
        if (resultData.containsKey(ApiService.ERROR_KEY)) {
            if (errorListener != null) {
                errorListener.onError();
            }
        } else {

            DataResponse response = (DataResponse) resultData
                    .getSerializable(ApiService.RESPONSE_OBJECT_KEY);
            if (responseListener != null) {
                responseListener.onResponse(response.getId(),operationID);
            }
        }
    }

    /**
     * Retrieves an image specified by the URL, displays it in the UI.
     * @param imageView view for displaying image
     * @param url string with url of image
     */
    protected void sendRequestPhoto(final ImageView imageView,String url){
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                       Log.d(DEBUG_TAG,error.getMessage());
                    }
                });
// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(request);
    }

}