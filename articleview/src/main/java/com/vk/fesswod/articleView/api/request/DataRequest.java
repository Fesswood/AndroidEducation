package com.vk.fesswod.articleView.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DataRequest implements Serializable {
    private long id;
    private String data;
    private String uri;

    public DataRequest(){
        this(0, null);
    }

    public DataRequest(long id){
        this(id, null,null);
    }

    public DataRequest(String data){
        this(0, data,null);
    }

    public DataRequest(long id, String data){
        this(id,data,null);
    }

    public DataRequest(String data, String uri){
        this(0,data,uri);
    }

    public DataRequest(long id, String data, String uri){
        this.id		= id;
        this.data	= data;
        this.uri	= uri;
    }

    public long		getId(){ return id;}
    public String 	getData(){ return data;}
    public String 	getAdditionalData(){ return uri;}


}
