
package com.tfltravelalerts.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.tfltravelalerts.TflApplication;

/**
 * A description of the {@link LineStatus} of a {@link Line}. Also includes a
 * text description.
 */
public class LineStatusUpdate {

    @SerializedName("details")
    private final String mDescription;

    @SerializedName("line")
    private final Line mLine;
    
    @SerializedName("state")
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

    @Override
    public String toString() {
        Context context = TflApplication.getLastInstance();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mLine);
        stringBuilder.append(": ");
        if(mLineStatus == null) {
            //just in case a new status comes up
            stringBuilder.append("unknown");
        } else {
            stringBuilder.append(context.getString(mLineStatus.getStatusResId()));
        }
        if (mDescription.length() > 0) {
            stringBuilder.append(" - ");
            stringBuilder.append(mDescription);
        }

        return stringBuilder.toString();
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
