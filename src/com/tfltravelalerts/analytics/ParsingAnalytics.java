package com.tfltravelalerts.analytics;

import android.os.SystemClock;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.tfltravelalerts.BuildConfig;

public class ParsingAnalytics {
    private String name;
    private long startTime;
    private String category;
    private boolean finished = false;
    
    public ParsingAnalytics(String category, String name) {
        this.name = name;
        this.category = category;
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
        tracker.sendTiming(category, timeInMs , name, Integer.toString(numberOfParsedObjects));
    }
}
