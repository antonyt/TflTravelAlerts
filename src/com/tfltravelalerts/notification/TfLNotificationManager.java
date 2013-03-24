
package com.tfltravelalerts.notification;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Application;
import org.holoeverywhere.util.SparseArray;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.tfltravelalerts.MainActivity;
import com.tfltravelalerts.R;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.alerts.events.AddOrUpdateAlertRequest;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.alerts.service.AlertsStore;
import com.tfltravelalerts.model.DayTime;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatus;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;
import com.tfltravelalerts.model.LineStatusAlertUtil;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;
import com.tfltravelalerts.statusviewer.service.LineStatusStore;

public class TfLNotificationManager {
    private final String LOG_TAG = "TfLNotificationManager";
    private final String NOTIFICATION_TAG = "TfLNotificationManager";
    private LineStatusAlertSet mAlerts;
    private LineStatusUpdateSet mLineStatus;
    private SparseArray<LineStatusUpdateSet> mNotifiedUpdates;

    public TfLNotificationManager() {
        mAlerts = AlertsStore.load();
        mLineStatus = LineStatusStore.load();
        mNotifiedUpdates = new SparseArray<LineStatusUpdateSet>();
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
        Log.d(LOG_TAG, "on AddOrUpdateAlertRequest - removing information about alert "+alertId);
        mNotifiedUpdates.remove(alertId);
    }
    private void checkNotifications() {

        // things to do:
        // -: DONE - fire new notifications
        // -: DONE - update notifications when values change
        // 3: repeat notifications that if values change [Done, just checking]
        // 4: remove notifications when normal status is back (?)
        // 5: remove notifications when notification period ends (?)
        // -: DONE - tapping notification opens alert view / current status
        // 7: option to show notifications only if there are disruptions
        // 8: fix icon in ticker
        // -: DONE - do not repeat notifications if they have been dismissed and there
        //    is no new data (even if we get fresh data)
        // 10: checkNotifications gets triggered if you refresh data and have no
        //     internet connectivity [probably doesn't make a difference any more]
        // 11: ensure a sane activity stack when you open the notification
        //     [Done? - just check]
        //  -: DONE - when you edit an alert, remove it from the list of notified
        //     alerts because its rules may have changed
        // 13: when the point above occurs, dismiss the notification as well?
        // 14: when an alert comes to an end, we need to clear it from the 
        //     notified alerts as well otherwise it may hide a notification
        //     from one day to another
        // 15: use persistence instead of relying on app not getting killed

        DayTime now = DayTime.now();
        for (LineStatusAlert alert : mAlerts.getAlerts()) {
            boolean active = LineStatusAlertUtil.alertActiveForTime(alert, now);
            if (active) {
                showOrUpdateNotification(alert);
            }

        }

    }

    private void showOrUpdateNotification(LineStatusAlert alert) {
        if (mLineStatus == null) {
            Log.w(LOG_TAG, "showOrUpdateNotification: mLineStatus is null. Not processing alert "
                    + alert.getId());
            return;
        }
        ImmutableSet<Line> lines = alert.getLines();
        LineStatusUpdateSet lastNotifiedUpdate = mNotifiedUpdates.get(alert.getId());
        List<LineStatusUpdate> disruptions = new ArrayList<LineStatusUpdate>(lines.size());
        int newProblemsFound = 0;
        int problemsResolved = 0;
        for (Line line : lines) {
            LineStatusUpdate newUpdateForLine = mLineStatus.getUpdateForLine(line);
            if (newUpdateForLine.getLineStatus() != LineStatus.GOOD_SERVICE) {
                disruptions.add(newUpdateForLine);
            }

            if (lastNotifiedUpdate != null) {
                LineStatusUpdate oldUpdateForLine = lastNotifiedUpdate.getUpdateForLine(line);
                if (newUpdateForLine.foundNewProblemSince(oldUpdateForLine)) {
                    newProblemsFound++;
                }
                if (newUpdateForLine.problemResolvedSince(oldUpdateForLine)) {
                    problemsResolved++;
                }
            }
        }
        
        Log.d(LOG_TAG, "showOrUpdateNotification for alert " + alert.getId() + " new problems = "
                + newProblemsFound + " and resolved " + problemsResolved);
        if(lastNotifiedUpdate == null || problemsResolved > 0 || newProblemsFound > 0) {
            Log.i(LOG_TAG, "showOrUpdateNotification: creating notification");
            Application context = TflApplication.getLastInstance();
            Notification notification = prepareNotification(alert, disruptions);
            NotificationManager nm = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(NOTIFICATION_TAG, alert.getId(), notification);
            mNotifiedUpdates.put(alert.getId(), mLineStatus);
        } else {
            Log.i(LOG_TAG, "showOrUpdateNotification: not showing notification due to no new data");
        }
    }

    private static Notification prepareNotification(LineStatusAlert alert,
            List<LineStatusUpdate> disruptions) {
        Application context = TflApplication.getLastInstance();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        String title = context.getString(R.string.notifications_title, alert.getTitle());
        builder.setContentTitle(title);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(true);
        PendingIntent pendingIntent = getPendingIntentForAlert(alert, context);
        builder.setContentIntent(pendingIntent);

        StringBuilder sb = new StringBuilder();
        String fullMessage = null;
        String shortMessageVersion = null;
        if (disruptions.size() == 0) {
            fullMessage = context.getString(R.string.notifications_good_service);
        } else if (disruptions.size() == 1) {
            disruptions.get(0).writeStatusLine(sb, context);
            fullMessage = sb.toString();
        } else {
            // more than one disruption
            for (LineStatusUpdate update : disruptions) {
                update.writeStatusLine(sb, context);
                sb.append('\n');
            }
            fullMessage = sb.toString();
            String lines = Joiner.on(", ").join(getLinesFromDisruptions(disruptions));
            shortMessageVersion = context.getString(
                    R.string.notifications_problem_list_short_message, lines);
        }
        if (shortMessageVersion == null) {
            shortMessageVersion = fullMessage;
        }
        builder.setTicker(shortMessageVersion);
        builder.setContentText(shortMessageVersion);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(fullMessage));
        Notification notification = builder.build();
        return notification;
    }

    private static PendingIntent getPendingIntentForAlert(LineStatusAlert alert, Application context) {
        Intent intent = new Intent(context, MainActivity.class);
        // starting activities from pending intents requires to use a new task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, alert.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private static List<String> getLinesFromDisruptions(List<LineStatusUpdate> disruptions) {
        ArrayList<String> ret = new ArrayList<String>(disruptions.size());
        for (LineStatusUpdate disruption : disruptions) {
            ret.add(disruption.getLine().toString());
        }
        return ret;
    }
}
