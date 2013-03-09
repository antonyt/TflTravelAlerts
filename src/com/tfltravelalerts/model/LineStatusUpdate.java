
package com.tfltravelalerts.model;

import java.util.Date;

/**
 * A description of the {@link LineStatus} of a {@link Line} at a particular
 * time. Also includes a text description.
 */
public class LineStatusUpdate {

    private final Date mDate;
    private final String mDescription;

    private final Line mLine;
    private final LineStatus mLineStatus;

    public LineStatusUpdate(Line line, LineStatus lineStatus, Date date, String description) {
        mLine = line;
        mLineStatus = lineStatus;
        mDate = date;
        mDescription = description;
    }

    public Line getLine() {
        return mLine;
    }

    public LineStatus getLineStatus() {
        return mLineStatus;
    }

    public String getDescription() {
        return mDescription;
    }

    public Date getDate() {
        return mDate;
    }

}
