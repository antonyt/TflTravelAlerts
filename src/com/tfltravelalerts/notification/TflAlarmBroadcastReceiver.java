
package com.tfltravelalerts.notification;

import org.holoeverywhere.widget.Toast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

    public static final String ALARM_ACTION = "com.tfltravelalerts.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received", Toast.LENGTH_SHORT).show();

        EventBus.getDefault().postSticky(new AlertTriggerEvent());
        EventBus.getDefault().postSticky(new LineStatusUpdateRequest());
    }

}
