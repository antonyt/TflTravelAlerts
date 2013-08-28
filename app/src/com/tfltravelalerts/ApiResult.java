
package com.tfltravelalerts;

import org.apache.http.HttpStatus;

import com.tfltravelalerts.common.eventbus.DataEvent;

public class ApiResult<T> extends DataEvent<T> {

    private final int mStatusCode;

    public ApiResult(int statusCode, T data) {
        super(data);
        mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public boolean isSuccess() {
        return mStatusCode == HttpStatus.SC_OK;
    }

}
