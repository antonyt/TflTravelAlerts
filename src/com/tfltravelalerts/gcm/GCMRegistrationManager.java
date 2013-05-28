
package com.tfltravelalerts.gcm;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.common.base.Joiner;
import com.tfltravelalerts.alerts.events.AlertTriggerEvent;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.model.DayTime;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;
import com.tfltravelalerts.notification.RegisterForPushNotificationsRequest;

import de.greenrobot.event.EventBus;

public class GCMRegistrationManager {
    public static final String SENDER_ID = "169890059213";
    private static final String LOG_TAG = "GCMRegistrationManager";
    private LineStatusAlertSet mLineStatusAlertSet;

    public GCMRegistrationManager() {
        EventBus.getDefault().registerSticky(this);
    }

    public void onEvent(AlertTriggerEvent event) {
        Log.d(LOG_TAG, "on AlertTriggerEvent for alert " + event.getAlertId());
        triggerPushNotificationRequest(event.getAlertId());
    }

    public void onEvent(AlertsUpdatedEvent update) {
        Log.d(LOG_TAG, "on AlertsUpdatedEvent");
        mLineStatusAlertSet = update.getData();
        for (LineStatusAlert alert : mLineStatusAlertSet.getActiveAlerts(DayTime.now())) {
            Log.i(LOG_TAG, "on AlertsUpdatedEvent: registering for alert " + alert.getId());
            triggerPushNotificationRequest(alert.getId());
        }
    }

    private void triggerPushNotificationRequest(int alertId) {
        if (mLineStatusAlertSet == null) {
            Log.w(LOG_TAG, "triggerPushNotificationRequest: mLineStatusAlertSet is null; skipping");
            return;
        }
        LineStatusAlert alert = mLineStatusAlertSet.getAlertById(alertId);
        if (alert == null) {
            Log.e(LOG_TAG, "failed to get alert from trigger event");
        } else {
            String lines = getLinesString(alert);
            EventBus.getDefault().post(new RegisterForPushNotificationsRequest(lines));
        }
    }

    private String getLinesString(LineStatusAlert alert) {
        List<String> list = new ArrayList<String>(alert.getLines().size());
        for (Line l : alert.getLines()) {
            // we do this because we want to use the value of the enum; not
            // the name the user sees
            list.add(l.name());
        }
        String linesString = Joiner.on(',').join(list);
        return linesString;
    }
}
