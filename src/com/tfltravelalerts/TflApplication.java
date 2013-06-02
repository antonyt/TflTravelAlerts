
package com.tfltravelalerts;

import java.lang.Thread.UncaughtExceptionHandler;

import org.holoeverywhere.app.Application;

import android.os.AsyncTask;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.tfltravelalerts.alerts.service.AlertsManager;
import com.tfltravelalerts.debug.ExceptionViewerUtils;
import com.tfltravelalerts.gcm.GCMHandleNotifier;
import com.tfltravelalerts.gcm.GCMRegistrationManager;
import com.tfltravelalerts.notification.TfLNotificationManager;
import com.tfltravelalerts.notification.TflAlarmManager;
import com.tfltravelalerts.statusviewer.service.LineStatusManager;
import com.tfltravelalerts.weekend.service.WeekendStatusManager;

public class TflApplication extends Application implements UncaughtExceptionHandler {

    private final String LOG_TAG = "TflApplication";

    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "TflApplication.onCreate - starting new application object");

        fixAsyncTaskBug();
        //we need to set the tracker here because some of the managers will trigger analytics already
        EasyTracker.getInstance().setContext(this);
        initializeManagers();

        if (BuildConfig.DEBUG) {
            mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    private void fixAsyncTaskBug() {
        // https://code.google.com/p/android/issues/detail?id=20915
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
              return null;
            }
          }.execute();        
    }

    private void initializeManagers() {
        new TfLNotificationManager();
        new TflAlarmManager();
        new AlertsManager();
        new LineStatusManager();
        new WeekendStatusManager();
        new GCMHandleNotifier();
        new GCMRegistrationManager();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ExceptionViewerUtils.appendException(ex);
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

}
