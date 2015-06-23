package com.vk.fesswod.articleView.api.response;

import java.io.Serializable;

public class DataResponse implements Serializable {
    private long id;
    public DataResponse(){
        id = 0;
    }

    public DataResponse(long id){
        this.id	= id;
    }

    public long getId(){
        return id;
    }

    public DataResponse[] newArray(int size) {
            return new DataResponse[size];
    }
}
