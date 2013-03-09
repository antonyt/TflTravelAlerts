
package com.tfltravelalerts;

import org.holoeverywhere.app.Application;

import android.content.Intent;

import com.tfltravelalerts.statusviewer.LineStatusService;

public class TflApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Intent service = new Intent(this, LineStatusService.class);
        startService(service);
    }
}