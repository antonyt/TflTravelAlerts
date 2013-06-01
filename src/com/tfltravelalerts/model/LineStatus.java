
package com.tfltravelalerts.model;

import com.tfltravelalerts.R;

/**
 * Possible status for a London Underground line.
 */
public enum LineStatus {
    GOOD_SERVICE(R.string.line_status_good_service, R.drawable.line_status_good_service),
    MINOR_DELAYS(R.string.line_status_minor_delays, R.drawable.line_status_minor_delays),
    SEVERE_DELAYS(R.string.line_status_severe_delays, R.drawable.line_status_severe_delays),
    PART_CLOSURE(R.string.line_status_part_closure, R.drawable.line_status_interruptions),
    PLANNED_CLOSURE(R.string.line_status_planned_closure, R.drawable.line_status_interruptions),
    SUSPENDED(R.string.line_status_suspended, R.drawable.line_status_interruptions),
    PART_SUSPENDED(R.string.line_status_part_suspended, R.drawable.line_status_interruptions),
    REDUCED_SERVICE(R.string.line_status_reduced_service, R.drawable.line_status_interruptions),
    BUS_SERVICE(R.string.line_status_bus_service, R.drawable.line_status_interruptions);

    private final int mStatusResId;
    private final int mStatusIcon;

    private LineStatus(int statusResId, int statusIcon) {
        mStatusResId = statusResId;
        mStatusIcon = statusIcon;
    }


    public int getStatusResId() {
        return mStatusResId;
    }
    
    public int getStatusIcon() {
        return mStatusIcon;
    }
}
