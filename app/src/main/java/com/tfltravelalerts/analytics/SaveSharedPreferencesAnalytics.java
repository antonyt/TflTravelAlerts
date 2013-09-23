package com.tfltravelalerts.analytics;

import android.os.SystemClock;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.tfltravelalerts.BuildConfig;

public class SaveSharedPreferencesAnalytics {
    private static final String CATEGORY_SHARED_PREFS = "saving to shared preferences";
    private static final String CATEGORY_SERIALIZING = "serializing";
    private static final String CATEGORY_BOTH = "serializing-and-saving-to-shared-preferences";
    private String name;
    private long startTime;
    private boolean finished = false;
    private long firstStepTime;
    
    public SaveSharedPreferencesAnalytics(String name) {
        this.name = name;
        startTime = SystemClock.elapsedRealtime();
    }
    
    public void serializedObject() {
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
        tracker.sendTiming(CATEGORY_SERIALIZING, firstStepTime-startTime , name, Integer.toString(numberOfParsedObjects));
        tracker.sendTiming(CATEGORY_SHARED_PREFS, finalTime - firstStepTime, name, Integer.toString(numberOfParsedObjects));
        tracker.sendTiming(CATEGORY_BOTH, finalTime-startTime , name, Integer.toString(numberOfParsedObjects));
    }
}
