
package com.tfltravelalerts.model;

import android.content.Context;

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
    
    public boolean isLineDisrupted() {
        return !LineStatus.GOOD_SERVICE.equals(getLineStatus());
    }

    public boolean problemResolvedSince(LineStatusUpdate lastUpdate) {
        return mLineStatus == LineStatus.GOOD_SERVICE && !this.equals(lastUpdate);
    }

    public boolean foundNewProblemSince(LineStatusUpdate lastUpdate) {
        return mLineStatus != LineStatus.GOOD_SERVICE && !this.equals(lastUpdate);
    }
    
    public void writeStatusLine(StringBuilder sb, Context c) {
        sb.append(mLine);
        sb.append(": ");
        sb.append(c.getString(mLineStatus.getStatusResId()));

        if (mDescription.length() > 0) {
            sb.append(" - ");
            sb.append(mDescription);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LineStatusUpdate) {
            LineStatusUpdate otherUpdate = (LineStatusUpdate) o;
            return mLine.equals(otherUpdate.getLine())
                    && mLineStatus.equals(otherUpdate.getLineStatus())
                    && mDescription.equals(otherUpdate.getDescription());
        }
        return false;
    }

}
