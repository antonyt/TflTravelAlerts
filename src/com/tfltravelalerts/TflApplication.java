
package com.tfltravelalerts;

import org.holoeverywhere.app.Application;

import android.content.Intent;
import android.util.Log;

import com.tfltravelalerts.alerts.service.AlertsService;
import com.tfltravelalerts.notification.TfLNotificationManager;
import com.tfltravelalerts.notification.TflAlarmManager;
import com.tfltravelalerts.statusviewer.service.LineStatusService;

import de.greenrobot.event.EventBus;

public class TflApplication extends Application {
    private final String LOG_TAG = "TflApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "TflApplication.onCreate - starting new application object");
        Intent lineStatusService = new Intent(this, LineStatusService.class);
        startService(lineStatusService);

        Intent alertsService = new Intent(this, AlertsService.class);
        startService(alertsService);

        TfLNotificationManager tflNotificationManager = new TfLNotificationManager();
        EventBus.getDefault().registerSticky(tflNotificationManager);

        TflAlarmManager tflAlarmManager = new TflAlarmManager();
        EventBus.getDefault().registerSticky(tflAlarmManager);
    }

}
