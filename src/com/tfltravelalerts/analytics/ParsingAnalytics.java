package com.tfltravelalerts.analytics;

import android.os.SystemClock;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.tfltravelalerts.BuildConfig;

public class ParsingAnalytics {
    /* package */ static final String CATEGORY = "parsing";
    private String name;
    private long startTime;
    private boolean finished = false;
    
    public ParsingAnalytics(String name) {
        this.name = name;
        startTime = SystemClock.elapsedRealtime();
    }
    
    @SuppressWarnings("unused")
    public void done(int numberOfParsedObjects) {
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
        tracker.sendTiming(CATEGORY, timeInMs , name, Integer.toString(numberOfParsedObjects));
    }
}
