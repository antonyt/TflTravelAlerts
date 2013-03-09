
package com.tfltravelalerts.model;


/**
 * A description of the {@link LineStatus} of a {@link Line}. Also includes a
 * text description.
 */
public class LineStatusUpdate {

    private final String mDescription;

    private final Line mLine;
    private final LineStatus mLineStatus;

    public LineStatusUpdate(Line line, LineStatus lineStatus, String description) {
        mLine = line;
        mLineStatus = lineStatus;
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

}
