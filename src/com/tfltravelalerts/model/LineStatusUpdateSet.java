
package com.tfltravelalerts.model;

import java.util.Date;
import java.util.List;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class LineStatusUpdateSet {

    private static final String LOG_TAG = "LineStatusUpdateSet";
    
    private final Date mDate;
    private final ImmutableList<LineStatusUpdate> mLineStatusUpdates;

    public LineStatusUpdateSet(Date date, List<LineStatusUpdate> lineStatusUpdates) {
        mDate = date;
        mLineStatusUpdates = ImmutableList.copyOf(lineStatusUpdates);
    }

    public Date getDate() {
        return mDate;
    }

    public List<LineStatusUpdate> getLineStatusUpdates() {
        return mLineStatusUpdates;
    }

    public LineStatusUpdate getUpdateForLine(Line line) {
        for (LineStatusUpdate lineStatusUpdate : mLineStatusUpdates) {
            if (lineStatusUpdate.getLine() == line) {
                return lineStatusUpdate;
            }
        }
        return null;
    }
    
    public LineStatusUpdateSet getUpdatesForAlert(LineStatusAlert alert) {
        Builder<LineStatusUpdate> builder = ImmutableList. <LineStatusUpdate>builder();
        for(LineStatusUpdate lineStatusUpdate : mLineStatusUpdates) {
            if(alert.getLines().contains(lineStatusUpdate.getLine())) {
                builder.add(lineStatusUpdate);
            }
        }
        return new LineStatusUpdateSet(mDate, builder.build());
    }

    public LineStatusUpdateSet getDisruptionUpdates() {
        Builder<LineStatusUpdate> builder = ImmutableList. <LineStatusUpdate>builder();
        for(LineStatusUpdate lineStatusUpdate : mLineStatusUpdates) {
            if(lineStatusUpdate.isLineDisrupted()) {
                builder.add(lineStatusUpdate);
            }
        }
        return new LineStatusUpdateSet(mDate, builder.build());
    }

    /**
     * Used in main screen to know if we should refresh data
     * @return
     */
    public boolean isOldResult() {
        int leniencyInterval = 1 * 60 * 60 * 1000;
        long then = mDate.getTime(); 
        long now = System.currentTimeMillis();
        return (now - then) > leniencyInterval;
    }
    
    /**
     * Used when we get new line statuses and then compare this status
     * with the latest notified status to know if there are any changes.
     * 
     * If this method returns false, it means that the data is too old
     * to be compared with the current data.
     * @return
     */
    public boolean isExpiredResult() {
        int leniencyInterval = 2 * 60 * 60 * 1000;
        long then = mDate.getTime(); 
        long now = System.currentTimeMillis();
        return (now - then) > leniencyInterval;
    }
    
    public int newProblemsFound(LineStatusUpdateSet newUpdateSet) {
        int count = 0;
        for(LineStatusUpdate lineStatusUpdate : mLineStatusUpdates) {
            LineStatusUpdate newUpdate = newUpdateSet.getUpdateForLine(lineStatusUpdate.getLine());
            if(newUpdate != null && newUpdate.foundNewProblemSince(lineStatusUpdate)) {
                count++;
            }
        }
        Log.d(LOG_TAG, "new problems found = " + count);
        return count;
    }

    public int oldProblemsResolved(LineStatusUpdateSet newUpdateSet) {
        int count = 0;
        for(LineStatusUpdate lineStatusUpdate : mLineStatusUpdates) {
            LineStatusUpdate newUpdate = newUpdateSet.getUpdateForLine(lineStatusUpdate.getLine());
            if(newUpdate != null && newUpdate.problemResolvedSince(lineStatusUpdate)) {
                count++;
            }
        }
        Log.d(LOG_TAG, "old problems resolved = " + count);
        return count;
    }

    public boolean lineStatusChanged(LineStatusUpdateSet newUpdateSet) {
        return newProblemsFound(newUpdateSet) > 0 || oldProblemsResolved(newUpdateSet) > 0;
    }
}
