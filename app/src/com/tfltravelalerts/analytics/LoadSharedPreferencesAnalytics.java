package com.tfltravelalerts.analytics;

import android.os.SystemClock;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.tfltravelalerts.BuildConfig;

public class LoadSharedPreferencesAnalytics {
    private static final String CATEGORY_SHARED_PREFS = "loading from shared preferences";
    private static final String CATEGORY_BOTH = "loading-and-parsing-shared-preferences";
    private String name;
    private long startTime;
    private boolean finished = false;
    private long firstStepTime;
    
    public LoadSharedPreferencesAnalytics(String name) {
        this.name = name;
        startTime = SystemClock.elapsedRealtime();
    }
    
    public void loadedFromPreferences() {
        firstStepTime = SystemClock.elapsedRealtime();
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
        
        long finalTime = SystemClock.elapsedRealtime();
        tracker.sendTiming(CATEGORY_SHARED_PREFS, firstStepTime-startTime , name, Integer.toString(numberOfParsedObjects));
        tracker.sendTiming(ParsingAnalytics.CATEGORY, finalTime - firstStepTime, name, Integer.toString(numberOfParsedObjects));
        tracker.sendTiming(CATEGORY_BOTH, finalTime-startTime , name, Integer.toString(numberOfParsedObjects));
    }
}
