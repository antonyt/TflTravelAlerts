
package com.tfltravelalerts.notification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;

import com.tfltravelalerts.R;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.alerts.events.AddOrUpdateAlertRequest;
import com.tfltravelalerts.alerts.events.AlertTimeoutEvent;
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
        SparseArray<LineStatusUpdateSet> notifiedUpdates = mNotificationManagerStore.load();
        if (notifiedUpdates == null) {
            Log.d(LOG_TAG, "constructor: found no notified updates");
            mNotifiedUpdates = new SparseArray<LineStatusUpdateSet>();
        } else {
            Log.d(LOG_TAG, "constructor: loading notified updates with a size of " + notifiedUpdates.size());
            mNotifiedUpdates = notifiedUpdates;
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

    public void onEvent(AlertTimeoutEvent timeout) {
        NotificationManager nm = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = buildTimeoutNotification();
        nm.notify(NOTIFICATION_TAG, timeout.getAlertId(), notification);
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
            Notification notification = buildAlertNotification(alert, mLineStatus);
            nm.notify(NOTIFICATION_TAG, alert.getId(), notification);
            mNotifiedUpdates.put(alert.getId(), mLineStatus);
            mNotificationManagerStore.save(mNotifiedUpdates);
        } else {
            Log.i(LOG_TAG, "showOrUpdateNotification: not showing notification due to no new data");
        }
    }

    private boolean shouldShowNotification(LineStatusAlert alert) {
        LineStatusUpdateSet notifiedUpdateSet = mNotifiedUpdates.get(alert.getId());
        LineStatusUpdateSet oldUpdateSet = null;
        if(notifiedUpdateSet != null) {
            oldUpdateSet = notifiedUpdateSet.getUpdatesForAlert(alert);
        }
        if(mLineStatus == null) {
            // edge case where the app was recently installed, was offline and we never got any
            // status - skip the notification for this time
            Log.d(LOG_TAG, "shouldShowNotification for alert " + alert.getId()
                    + "; no line status yet. Skip this time");
            return false;
        }
        LineStatusUpdateSet currentUpdateSet = mLineStatus.getUpdatesForAlert(alert);

        if(alert.onlyNotifyForDisruptions() && !currentUpdateSet.isDisrupted()) {
            if(oldUpdateSet != null && oldUpdateSet.isDisrupted()) {
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

    private Notification buildAlertNotification(LineStatusAlert alert,
            LineStatusUpdateSet lineStatusUpdateSet) {
        return new TflNotificationBuilder(alert, mLineStatus).buildNotification();
    }

    private Notification buildTimeoutNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        String title = mContext.getString(R.string.notifications_timeout_title);
        builder.setContentTitle(title);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_notification_small);
        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_notification_large);
        builder.setLargeIcon(largeIcon);
        builder.setAutoCancel(true);

        PendingIntent pendingIntent = TflNotificationBuilder.getPendingIntent(mContext, 0);
        builder.setContentIntent(pendingIntent);

        String message = mContext.getString(R.string.notifications_timeout_message);
        builder.setTicker(message);
        builder.setContentText(message);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        return notification;
    }

}
