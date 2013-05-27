package com.tfltravelalerts.common.requests;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import com.google.common.base.Joiner;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class BackendConnectionResult {
    public final int statusCode;
    public final String statusMessage;
    public final IOException exception;
    public final InputStream inputStream; 
    private final AndroidHttpClient httpClient;
    
    public BackendConnectionResult(AndroidHttpClient httpClient, IOException exception) {
        this.exception = exception;
        this.httpClient = httpClient;
        statusMessage = "IOException thrown: "+exception.getMessage();
        statusCode = -1;
        inputStream = null;
    }
    
    public BackendConnectionResult(AndroidHttpClient httpClient, StatusLine statusLine, InputStream inputStream) {
        this.inputStream = inputStream;
        this.statusCode = statusLine.getStatusCode();
        this.statusMessage = statusLine.getReasonPhrase();
        this.httpClient = httpClient;
        exception = null;
    }
    
    public boolean isHttpStatusOk() {
        return statusCode == HttpStatus.SC_OK;
    }

    /**
     * Logs the error using the provided tag. If there was no error, no output will be written
     * @param tag
     */
    public void logError(String tag, String message) {
        if( exception != null) {
            Log.e(tag, message, exception);
        } else {
            String composed = Joiner.on(' ').join(message, '-', statusCode, statusMessage);
            Log.e(tag, composed);
        }
    }
    
    public void close() {
        httpClient.close();
    }
}
