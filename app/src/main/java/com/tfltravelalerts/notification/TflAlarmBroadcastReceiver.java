
package com.tfltravelalerts.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tfltravelalerts.alerts.events.AlertTriggerEvent;
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
 
    @Override
    public void onReceive(Context context, Intent intent) {
        int alertId = intent.getIntExtra(TflAlarmManager.ALERT_ID_FIELD, -1);
        Log.i(LOG_TAG, "Alarm received for alert "+alertId);
        EventBus.getDefault().postSticky(new AlertTriggerEvent(alertId));
        EventBus.getDefault().postSticky(new LineStatusUpdateRequest());
    }

}
