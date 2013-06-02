
package com.tfltravelalerts.analytics;

import com.google.analytics.tracking.android.EasyTracker;

public class EventAnalytics {
    private static final String CATEGORY_SHOULD_NOT_HAPPEN = "should-not-happen";
    private static final String CATEGORY_PROBLEM = "problems";

    static public void thisShouldNotHappen(String what) {
        thisShouldNotHappen(what, "-empty-");
    }

    static public void thisShouldNotHappen(String what, String description) {
        EasyTracker.getTracker().sendEvent(CATEGORY_SHOULD_NOT_HAPPEN, what, description, null);
    }

    static public void thisShouldNotHappen(String what, Throwable throwable) {
        String description = getThrowableDescription(throwable);
        thisShouldNotHappen(what, description);
    }

    private static String getThrowableDescription(Throwable throwable) {
        String threadName = Thread.currentThread().getName();
        String description = EasyTracker.getTracker().getExceptionParser()
                .getDescription(threadName, throwable);
        return description;
    }

    static public void reportErrorCondition(String what, String description) {
        EasyTracker.getTracker().sendEvent(CATEGORY_PROBLEM, what, description, null);
    }
    
    static public void reportErrorCondition(String what, Throwable throwable) {
        String description = getThrowableDescription(throwable);
        reportErrorCondition(what, description);
    }
}
