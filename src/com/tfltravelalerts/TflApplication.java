
package com.tfltravelalerts;

import org.holoeverywhere.app.Application;

import android.content.Intent;

import com.tfltravelalerts.alerts.service.AlertsService;
import com.tfltravelalerts.statusviewer.service.LineStatusService;

public class TflApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Intent lineStatusService = new Intent(this, LineStatusService.class);
        startService(lineStatusService);

        Intent alertsService = new Intent(this, AlertsService.class);
        startService(alertsService);
    }
}