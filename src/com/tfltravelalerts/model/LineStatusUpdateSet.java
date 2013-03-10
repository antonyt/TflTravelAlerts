
package com.tfltravelalerts.model;

import java.util.Date;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class LineStatusUpdateSet {

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
    
    public boolean isOldResult() {
        int leniencyInterval = 1 * 60 * 60 * 1000;
        long then = mDate.getTime(); 
        long now = System.currentTimeMillis();
        return (now - then) > leniencyInterval;
        
    }
}
