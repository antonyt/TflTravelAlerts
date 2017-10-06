
package com.tfltravelalerts.notification;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.alerts.events.AlertDeletedEvent;
import com.tfltravelalerts.alerts.events.AlertTriggerEvent;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Class to setup alarms to refresh data according to alert times.
 */
public class TflAlarmManager {
    final static public String ALERT_ID_FIELD = "alert_id";

    final static private String LOG_TAG = "TflAlarmManager";
    private Application mContext;
    private LineStatusAlertSet mLineStatusAlertSet;

    public TflAlarmManager() {
        EventBus.getDefault().registerSticky(this);
        mContext = TflApplication.getLastInstance();
    }

    public void onEventMainThread(AlertsUpdatedEvent event) {
        mLineStatusAlertSet = event.getData();
        setAlarms();
    }

    public void onEventMainThread(AlertTriggerEvent event) {
        setAlarms();
        setTimeoutForAlert(event.getAlertId());
    }

    private void setTimeoutForAlert(int alertId) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        long now = System.currentTimeMillis();
        PendingIntent pendingIntent = makePendingIntentForTimeout(alertId);
        long triggerTime = System.currentTimeMillis() + TflAlarmBroadcastReceiver.TIMEOUT_DELAY_MS;
        alarmManager.set(AlarmManager.RTC, triggerTime, pendingIntent);

        Date date = new Date(triggerTime);
        Log.i(LOG_TAG, "Setting timeout alarm for " + alertId + " at " + date);
    }

    public void onEventMainThread(AlertDeletedEvent event) {
        removeAlarm(event.getData());
    }

    public void onEventMainThread(LineStatusUpdateSuccess event) {
        if (mLineStatusAlertSet == null) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        for (LineStatusAlert alert : mLineStatusAlertSet.getAlerts()) {
            PendingIntent pendingIntent = restorePendingIntentForTimeout(alert.getId());
            if(pendingIntent != null) {
                Log.d(LOG_TAG, "canceling timeout alarm for alert "+alert.getId());
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    private void removeAlarm(LineStatusAlert alert) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = makePendingIntentForAlert(alert);
        alarmManager.cancel(pendingIntent);
        Log.i(LOG_TAG, "removing alarm for alert " + alert.toString());
    }

    private void setAlarms() {
        if (mLineStatusAlertSet == null) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        long now = System.currentTimeMillis();
        for (LineStatusAlert alert : mLineStatusAlertSet.getAlerts()) {
            PendingIntent pendingIntent = makePendingIntentForAlert(alert);
            long triggerTime = alert.getNextAlertTime(now);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);

            Date date = new Date(triggerTime);
            Log.i(LOG_TAG, "setting alert for " + alert.toString() + " at " + date);
        }
    }

    private PendingIntent makePendingIntentForAlert(LineStatusAlert alert) {
        Intent intent = new Intent(TflAlarmBroadcastReceiver.ALARM_ACTION);
        intent.putExtra(ALERT_ID_FIELD, alert.getId());
        return PendingIntent.getBroadcast(mContext, alert.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent makePendingIntentForTimeout(int alertId) {
        Intent intent = new Intent(TflAlarmBroadcastReceiver.ALARM_TIMEOUT_ACTION);
        intent.putExtra(ALERT_ID_FIELD, alertId);
        return PendingIntent.getBroadcast(mContext, alertId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent restorePendingIntentForTimeout(int alertId) {
        Intent intent = new Intent(TflAlarmBroadcastReceiver.ALARM_TIMEOUT_ACTION);
        intent.putExtra(ALERT_ID_FIELD, alertId);
        return PendingIntent.getBroadcast(mContext, alertId, intent,
                PendingIntent.FLAG_NO_CREATE);
    }

}
