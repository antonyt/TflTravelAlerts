
package com.tfltravelalerts.model;

import java.util.Date;

/**
 * A description of the {@link LineStatus} of a {@link Line} at a particular
 * time. Also includes a text description.
 */
public class LineStatusUpdate {

    Date mDate;
    String mDescription;

    Line mLine;
    LineStatus mLineStatus;

    public Line getLine() {
        return mLine;
    }

    public void setLine(Line line) {
        mLine = line;
    }

    public LineStatus getLineStatus() {
        return mLineStatus;
    }

    public void setLineStatus(LineStatus lineStatus) {
        mLineStatus = lineStatus;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

}
