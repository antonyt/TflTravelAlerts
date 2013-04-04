
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

/**
 * TODO: write doc
 */
public class TfLNotificationManager {

    private static final String LOG_TAG = "TfLNotificationManager";
    private static final String NOTIFICATION_TAG = "TfLNotificationManager";

    private Context mContext;

    private LineStatusAlertSet mAlerts;
    private LineStatusUpdateSet mLineStatus;
    private SparseArray<LineStatusUpdateSet> mNotifiedUpdates;

    public TfLNotificationManager() {
        mContext = TflApplication.getLastInstance();
        SparseArray<LineStatusUpdateSet> notifiedUptates = TfLNotificationManagerStore.load();
        if(notifiedUptates == null) {
            mNotifiedUpdates = new SparseArray<LineStatusUpdateSet>();
        } else {
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
        TfLNotificationManagerStore.save(mNotifiedUpdates);
    }

    private void checkNotifications() {

        // things to do:
        // 7: option to show notifications only if there are disruptions
        // 8: fix icon in ticker
        // 11: ensure a sane activity stack when you open the notification
        // [Done? - just check]
        // 14: when an alert comes to an end, we need to clear it from the
        // notified alerts as well otherwise it may hide a notification
        // from one day to another

        DayTime now = DayTime.now();
        for (LineStatusAlert alert : mAlerts.getActiveAlerts(now)) {
            showOrUpdateNotification(alert);
        }
    }

    private void showOrUpdateNotification(LineStatusAlert alert) {
        if (mLineStatus == null) {
            Log.w(LOG_TAG, "showOrUpdateNotification: mLineStatus is null. Not processing alert "
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
            TfLNotificationManagerStore.save(mNotifiedUpdates);
        } else {
            Log.i(LOG_TAG, "showOrUpdateNotification: not showing notification due to no new data");
        }
    }

    private boolean shouldShowNotification(LineStatusAlert alert) {
        LineStatusUpdateSet notifiedUpdateSet = mNotifiedUpdates.get(alert.getId());
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
        
        LineStatusUpdateSet oldUpdateSet = notifiedUpdateSet.getUpdatesForAlert(alert);
        LineStatusUpdateSet currentUpdateSet = mLineStatus.getUpdatesForAlert(alert);

        Log.d(LOG_TAG, "shouldShowNotification for alert " + alert.getId()
                + "; checking for changes since last time...");
        return oldUpdateSet.lineStatusChanged(currentUpdateSet);
    }

    private Notification buildNotification(LineStatusAlert alert,
            LineStatusUpdateSet lineStatusUpdateSet) {
        return new TflNotificationBuilder(alert, mLineStatus).buildNotification();
    }

}
