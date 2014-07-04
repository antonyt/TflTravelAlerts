
package com.tfltravelalerts.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.common.base.Joiner;
import com.tfltravelalerts.MainActivity;
import com.tfltravelalerts.R;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateSet;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: write doc
 */
public class TflNotificationBuilder {

    private final Context mContext;

    private final LineStatusAlert mAlert;
    private final LineStatusUpdateSet mLineStatusUpdateSet;
    private final LineStatusUpdateSet mDisruptionUpdateSet;

    public TflNotificationBuilder(LineStatusAlert alert, LineStatusUpdateSet lineStatusUpdateSet) {
        mContext = TflApplication.getLastInstance();
        mAlert = alert;
        mLineStatusUpdateSet = lineStatusUpdateSet;
        mDisruptionUpdateSet = mLineStatusUpdateSet.getUpdatesForAlert(alert).getDisruptionUpdates();
    }

    public Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        String title = mContext.getString(R.string.notifications_title, mAlert.getTitle());
        builder.setContentTitle(title);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_notification_small);
        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_notification_large);
        builder.setLargeIcon(largeIcon);
        builder.setAutoCancel(true);

        PendingIntent pendingIntent = getPendingIntent(mContext, mAlert.getId());
        builder.setContentIntent(pendingIntent);

        String fullMessage = getFullAlertMessage();
        String shortMessage = getShortAlertMessage();
        builder.setTicker(shortMessage);
        builder.setContentText(shortMessage);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(fullMessage));

        Notification notification = builder.build();
        return notification;
    }

    private String getFullAlertMessage() {
        if (mDisruptionUpdateSet.getLineStatusUpdates().isEmpty()) {
            return mContext.getString(R.string.notifications_good_service);
        } else {
            return Joiner.on("\n").join(mDisruptionUpdateSet.getLineStatusUpdates());
        }
    }

    public String getShortAlertMessage() {
        if (mDisruptionUpdateSet.getLineStatusUpdates().size() <= 1) {
            return getFullAlertMessage();
        } else {
            List<String> lines = getLinesFromDisruptions(mDisruptionUpdateSet
                    .getLineStatusUpdates());
            String joinedLines = Joiner.on(", ").join(lines);
            return mContext.getString(R.string.notifications_problem_list_short_message,
                    joinedLines);
        }
    }

    public static PendingIntent getPendingIntent(Context context, int requestId) {
        Intent intent = new Intent(context, MainActivity.class);
        // starting activities from pending intents requires to use a new task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestId, intent,
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
