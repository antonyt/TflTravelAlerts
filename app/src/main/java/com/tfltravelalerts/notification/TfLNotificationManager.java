
package com.tfltravelalerts.notification;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.alerts.events.AddOrUpdateAlertRequest;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.model.DayTime;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

import de.greenrobot.event.EventBus;

public class TfLNotificationManager {

    private static final String LOG_TAG = "TfLNotificationManager";
    private static final String NOTIFICATION_TAG = "TfLNotificationManager";
    private final TfLNotificationManagerStore mNotificationManagerStore = new TfLNotificationManagerStore(); 

    private Context mContext;

    private LineStatusAlertSet mAlerts;
    private LineStatusUpdateSet mLineStatus;
    private SparseArray<LineStatusUpdateSet> mNotifiedUpdates;

    public TfLNotificationManager() {
        EventBus.getDefault().registerSticky(this);
        mContext = TflApplication.getLastInstance();
        SparseArray<LineStatusUpdateSet> notifiedUptates = mNotificationManagerStore.load();
        if (notifiedUptates == null) {
            Log.d(LOG_TAG, "contructor: found no notified updates");
            mNotifiedUpdates = new SparseArray<LineStatusUpdateSet>();
        } else {
            Log.d(LOG_TAG, "contructor: loading notified updates with a size of " + notifiedUptates.size());
            mNotifiedUpdates = notifiedUptates;
        }
    }

    public void onEvent(LineStatusUpdateSuccess update) {
        Log.d(LOG_TAG, "on LineStatusUpdateSuccess");
        mLineStatus = update.getData();
        checkNotifications();
    }

    public void onEvent(AlertsUpdatedEvent update) {
        Log.d(LOG_TAG, "on AlertsUpdatedEvent");
        mAlerts = update.getData();
        checkNotifications();
    }

    public void onEvent(AddOrUpdateAlertRequest update) {
        int alertId = update.getData().getId();
        Log.d(LOG_TAG, "on AddOrUpdateAlertRequest - removing information about alert " + alertId);
        mNotifiedUpdates.remove(alertId);
        mNotificationManagerStore.save(mNotifiedUpdates);
    }
    
    private void checkNotifications() {
        DayTime now = DayTime.now();
        if(mAlerts != null) {
            for (LineStatusAlert alert : mAlerts.getActiveAlerts(now)) {
                showOrUpdateNotification(alert);
            }
        }
    }

    private void showOrUpdateNotification(LineStatusAlert alert) {
        if (mLineStatus == null) {
            Log.d(LOG_TAG, "showOrUpdateNotification: mLineStatus is null. Not processing alert "
                    + alert.getId());
            return;
        }

        if (shouldShowNotification(alert)) {
            Log.i(LOG_TAG, "showOrUpdateNotification: creating notification");
            NotificationManager nm = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = buildNotification(alert, mLineStatus);
            nm.notify(NOTIFICATION_TAG, alert.getId(), notification);
            mNotifiedUpdates.put(alert.getId(), mLineStatus);
            mNotificationManagerStore.save(mNotifiedUpdates);
        } else {
            Log.i(LOG_TAG, "showOrUpdateNotification: not showing notification due to no new data");
        }
    }

    private boolean shouldShowNotification(LineStatusAlert alert) {
        LineStatusUpdateSet notifiedUpdateSet = mNotifiedUpdates.get(alert.getId());
        LineStatusUpdateSet oldUpdateSet = notifiedUpdateSet.getUpdatesForAlert(alert);
        LineStatusUpdateSet currentUpdateSet = mLineStatus.getUpdatesForAlert(alert);

        if(alert.onlyNotifyForDisruptions() && !currentUpdateSet.isDisrupted()) {
            if(oldUpdateSet.isDisrupted()) {
                Log.d(LOG_TAG, "shouldShowNotification for alert " + alert.getId()
                        + "; service just got back to normal. Notify user");
                return true;
            }
            Log.d(LOG_TAG, "shouldShowNotification for alert " + alert.getId()
                    + "; service is good - suppress notification");
            return false;
        }

        if (notifiedUpdateSet == null) {
            Log.d(LOG_TAG, "shouldShowNotification for alert " + alert.getId()
                    + "; no previous notification detected!");
            return true;
        }

        if(notifiedUpdateSet.isExpiredResult()) {
            Log.d(LOG_TAG, "shouldShowNotification for alert " + alert.getId()
                    + "; last result has expired");
            return true;
        }

        Log.d(LOG_TAG, "shouldShowNotification for alert " + alert.getId()
                + "; checking for changes since last time...");
        return oldUpdateSet.lineStatusChanged(currentUpdateSet);
    }

    private Notification buildNotification(LineStatusAlert alert,
            LineStatusUpdateSet lineStatusUpdateSet) {
        return new TflNotificationBuilder(alert, mLineStatus).buildNotification();
    }

}
