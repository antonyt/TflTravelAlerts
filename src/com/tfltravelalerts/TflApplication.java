
package com.tfltravelalerts;

import org.holoeverywhere.app.Application;

import android.util.Log;

import com.tfltravelalerts.alerts.service.AlertsManager;
import com.tfltravelalerts.notification.TfLNotificationManager;
import com.tfltravelalerts.notification.TflAlarmManager;
import com.tfltravelalerts.statusviewer.service.LineStatusManager;

public class TflApplication extends Application {
    private final String LOG_TAG = "TflApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "TflApplication.onCreate - starting new application object");

        initializeManagers();
    }

    private void initializeManagers() {
        new TfLNotificationManager();
        new TflAlarmManager();
        new AlertsManager();
        new LineStatusManager();
    }

}
