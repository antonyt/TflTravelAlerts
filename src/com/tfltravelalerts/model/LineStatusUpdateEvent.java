package com.tfltravelalerts.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LineStatusUpdateEvent {
    
    private final List<LineStatusUpdate> mLineStatusUpdates;
    private final Date mDate;
    
    public LineStatusUpdateEvent(Date date, LineStatusUpdate ... lineStatusUpdates) {
        mDate = date;
        mLineStatusUpdates = Arrays.asList(lineStatusUpdates);
    }
    
    public Date getDate() {
        return mDate;
    }
    
    public List<LineStatusUpdate> getLineStatusUpdates() {
        return mLineStatusUpdates;
    }
    
    public LineStatusUpdate getUpdateForLine(Line line) {
        for(LineStatusUpdate lineStatusUpdate : mLineStatusUpdates) {
            if (lineStatusUpdate.getLine() == line) {
                return lineStatusUpdate;
            }
        }
        return null;
    }
}
