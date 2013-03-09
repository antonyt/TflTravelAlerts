
package com.tfltravelalerts.model;

import com.tfltravelalerts.R;

/**
 * Possible status for a London Underground line.
 */
public enum LineStatus {
    // TODO: find out all possible statuses
    GOOD_SERVICE("GS", R.string.line_status_good_service),
    MINOR_DELAYS("MD", R.string.line_status_minor_delays),
    SEVERE_DELAYS("SD", R.string.line_status_severe_delays),
    PART_CLOSURE("PC", R.string.line_status_part_closure),
    PLANNED_CLOSURE("CS", R.string.line_status_planned_closure);

    private String mId;
    private int mStatusResId;

    private LineStatus(String id, int statusResId) {
        mId = id;
        mStatusResId = statusResId;
    }

    public static LineStatus getLineStatusById(String id) {
        for (LineStatus lineStatus : values()) {
            if (lineStatus.getId().equals(id)) {
                return lineStatus;
            }
        }
        return null;
    }

    public String getId() {
        return mId;
    }

    public int getStatusResId() {
        return mStatusResId;
    }
}
