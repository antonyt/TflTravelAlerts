
package com.tfltravelalerts.model;

import com.tfltravelalerts.R;

/**
 * Possible status for a London Underground line.
 */
public enum LineStatus {
    GOOD_SERVICE("GS", R.string.line_status_good_service),
    MINOR_DELAYS("MD", R.string.line_status_minor_delays),
    SEVERE_DELAYS("SD", R.string.line_status_severe_delays),
    PART_CLOSURE("PC", R.string.line_status_part_closure),
    PLANNED_CLOSURE("CS", R.string.line_status_planned_closure),
    SUSPENDED("SU", R.string.line_status_suspended),
    //TODO check ids with the following statuses
    PART_SUSPENDED("PS", R.string.line_status_part_suspended),
    REDUCED_SERVICE("RS", R.string.line_status_reduced_service),
    BUS_SERVICE("BS", R.string.line_status_bus_service);

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
