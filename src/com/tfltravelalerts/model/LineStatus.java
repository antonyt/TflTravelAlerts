
package com.tfltravelalerts.model;

import com.tfltravelalerts.R;

/**
 * Possible status for a London Underground line.
 */
public enum LineStatus {
    GOOD_SERVICE("GS", R.string.line_status_good_service, R.drawable.line_status_good_service),
    MINOR_DELAYS("MD", R.string.line_status_minor_delays, R.drawable.line_status_minor_delays),
    SEVERE_DELAYS("SD", R.string.line_status_severe_delays, R.drawable.line_status_severe_delays),
    PART_CLOSURE("PC", R.string.line_status_part_closure, R.drawable.line_status_interruptions),
    PLANNED_CLOSURE("CS", R.string.line_status_planned_closure, R.drawable.line_status_interruptions),
    SUSPENDED("SU", R.string.line_status_suspended, R.drawable.line_status_interruptions),
    //TODO check ids with the following statuses
    PART_SUSPENDED("PS", R.string.line_status_part_suspended, R.drawable.line_status_interruptions),
    REDUCED_SERVICE("RS", R.string.line_status_reduced_service, R.drawable.line_status_interruptions),
    BUS_SERVICE("BS", R.string.line_status_bus_service, R.drawable.line_status_interruptions);

    private String mId;
    private int mStatusResId;
    private int mStatusIcon;

    private LineStatus(String id, int statusResId, int statusIcon) {
        mId = id;
        mStatusResId = statusResId;
        mStatusIcon = statusIcon;
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
    
    public int getStatusIcon() {
        return mStatusIcon;
    }
}
