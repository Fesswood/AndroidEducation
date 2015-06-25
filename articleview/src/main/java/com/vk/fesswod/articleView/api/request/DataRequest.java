package com.vk.fesswod.articleView.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.vk.fesswod.articleView.data.Article;

import java.io.Serializable;

public class DataRequest implements Serializable {
    private long    mId;
    private Article mArticle;

    public DataRequest(){
        this(0, null);
    }

    public DataRequest(long id){
        this(id, null);
    }

    public DataRequest(Article article){
        this(0, article);
    }

    public DataRequest(long id, Article article){
        this.mId	    = id;
        this.mArticle	= article;
    }




    public long		getId()            { return mId;}
    public Article  getArticle()       { return mArticle;}


}
