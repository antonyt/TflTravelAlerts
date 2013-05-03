
package com.tfltravelalerts;

import java.lang.Thread.UncaughtExceptionHandler;

import org.holoeverywhere.app.Application;

import android.util.Log;

import com.tfltravelalerts.alerts.service.AlertsManager;
import com.tfltravelalerts.debug.ExceptionViewerUtils;
import com.tfltravelalerts.notification.TfLNotificationManager;
import com.tfltravelalerts.notification.TflAlarmManager;
import com.tfltravelalerts.statusviewer.service.LineStatusManager;

public class TflApplication extends Application implements UncaughtExceptionHandler {

    private final String LOG_TAG = "TflApplication";

    public static final boolean DEBUG = true;

    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "TflApplication.onCreate - starting new application object");

        initializeManagers();

        if (DEBUG) {
            mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    private void initializeManagers() {
        new TfLNotificationManager();
        new TflAlarmManager();
        new AlertsManager();
        new LineStatusManager();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ExceptionViewerUtils.appendException(ex);
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

}
