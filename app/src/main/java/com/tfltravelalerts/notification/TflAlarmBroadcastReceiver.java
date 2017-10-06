
package com.tfltravelalerts.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tfltravelalerts.alerts.events.AlertTimeoutEvent;
import com.tfltravelalerts.alerts.events.AlertTriggerEvent;
import com.tfltravelalerts.analytics.EventAnalytics;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;

import de.greenrobot.event.EventBus;

/**
 * Broadcast receiver responsible for reacting to alarm.
 * 
 * TODO: get wake lock
 * 
 */
public class TflAlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "TflAlarmBroadcastReceiver";
    public static final String ALARM_ACTION = "com.tfltravelalerts.alarm";
    public static final String ALARM_TIMEOUT_ACTION = "com.tfltravelalerts.alarm.timeout";

    /**
     * Timeout after which if we didn't receive any results we will notify the user that we are
     * having problems getting updates. Currently 45 minutes
     */
    public static final int TIMEOUT_DELAY_MS = 1000 * 60 * 45;
 
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int alertId = intent.getIntExtra(TflAlarmManager.ALERT_ID_FIELD, -1);
        if(TflAlarmBroadcastReceiver.ALARM_ACTION.equals(action)) {
            Log.i(LOG_TAG, "Alarm received for alert "+alertId);
            EventBus.getDefault().postSticky(new AlertTriggerEvent(alertId));
            EventBus.getDefault().postSticky(new LineStatusUpdateRequest());
        } else if(TflAlarmBroadcastReceiver.ALARM_TIMEOUT_ACTION.equals(action)) {
            Log.i(LOG_TAG, "Timeout received for alert "+alertId);
            EventBus.getDefault().post(new AlertTimeoutEvent(alertId));

        } else {
            EventAnalytics.thisShouldNotHappen("TflAlarmBroadcastReceiver did not recognize action", action);
        }
    }

}
