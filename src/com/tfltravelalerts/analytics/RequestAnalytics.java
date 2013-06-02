package com.tfltravelalerts.analytics;

import org.apache.http.client.methods.HttpUriRequest;

import android.os.SystemClock;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.tfltravelalerts.BuildConfig;
import com.tfltravelalerts.common.networkstate.NetworkState;
import com.tfltravelalerts.common.requests.BackendConnectionResult;

public class RequestAnalytics {
    private static final String CATEGORY = "requests";
    private String name;
    private long startTime;
    private boolean finished = false;
    
    public RequestAnalytics(HttpUriRequest request) {
        this.name = request.getURI().getPath();
        startTime = SystemClock.elapsedRealtime();
    }
    
    @SuppressWarnings("unused")
    public void done(BackendConnectionResult result) {
        if(finished) {
            if(BuildConfig.DEBUG) {
                //only crash if we are in a dev build
                throw new IllegalStateException("Timer already had finished before!");
            }
            return;
        }
        finished = true;
        Tracker tracker = EasyTracker.getTracker();
        long timeInMs = SystemClock.elapsedRealtime() - startTime;
        String label = getLabelString(result);
        tracker.sendTiming(CATEGORY, timeInMs , name, label);
    }
    
    private String getLabelString(BackendConnectionResult result) {
        return NetworkState.getNetworkType() + " " + result.statusMessage;
    }
   
}
