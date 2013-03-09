package com.tfltravelalerts;

import org.apache.http.HttpStatus;

public class ApiResult<T> {

    private final int mStatusCode;
    private final T mData;

    public ApiResult(int statusCode, T data) {
        mStatusCode = statusCode;
        mData = data;
    }
    
    public int getStatusCode() {
        return mStatusCode;
    }
    
    public boolean isSuccess() {
        return mStatusCode == HttpStatus.SC_OK;
    }
    
    public T getData() {
        return mData;
    }
    
}
