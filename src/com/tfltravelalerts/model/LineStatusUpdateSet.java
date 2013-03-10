
package com.tfltravelalerts.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LineStatusUpdateSet {

    private final Date mDate;
    private final List<LineStatusUpdate> mLineStatusUpdates;

    public LineStatusUpdateSet(Date date, List<LineStatusUpdate> lineStatusUpdates) {
        mDate = date;
        mLineStatusUpdates = new ArrayList<LineStatusUpdate>(lineStatusUpdates);
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
