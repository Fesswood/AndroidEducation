package com.vk.fesswod.articleView.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestImageUrlWrapper  implements Serializable{
    @SerializedName("url")
    private String mImageUrl;

    public RequestImageUrlWrapper() {
    }

    public RequestImageUrlWrapper(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}