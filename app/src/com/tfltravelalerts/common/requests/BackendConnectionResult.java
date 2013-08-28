package com.tfltravelalerts.common.requests;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import android.util.Log;

import com.google.common.base.Joiner;

public class BackendConnectionResult {
    public final int statusCode;
    public final String statusMessage;
    public final IOException exception;
    public final String content; 
    
    public BackendConnectionResult(IOException exception) {
        this.exception = exception;
        statusMessage = "IOException thrown: "+exception.getMessage();
        statusCode = -1;
        content = null;
    }
    
    public BackendConnectionResult(StatusLine statusLine, String content) {
        this.statusCode = statusLine.getStatusCode();
        this.statusMessage = statusLine.getReasonPhrase();
        this.content = content;
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
}
