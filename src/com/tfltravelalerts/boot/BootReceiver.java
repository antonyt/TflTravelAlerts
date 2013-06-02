
package com.tfltravelalerts.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.i(LOG_TAG, "received boot completed");
            // not because before we get to this point, our app
            // has been started already, this means that the
            // Alarm and Notification managers were started 
            // which will set alarms and trigger notifications
            // if needed
        }
    }
}
