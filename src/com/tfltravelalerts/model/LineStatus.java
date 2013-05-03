
package com.tfltravelalerts.model;

import com.tfltravelalerts.R;

/**
 * Possible status for a London Underground line.
 */
public enum LineStatus {
    GOOD_SERVICE("Good service", "GS", R.string.line_status_good_service, R.drawable.line_status_good_service),
    MINOR_DELAYS("Minor delays", "MD", R.string.line_status_minor_delays, R.drawable.line_status_minor_delays),
    SEVERE_DELAYS("Severe delays", "SD", R.string.line_status_severe_delays, R.drawable.line_status_severe_delays),
    PART_CLOSURE("Part closure", "PC", R.string.line_status_part_closure, R.drawable.line_status_interruptions),
    PLANNED_CLOSURE("Planned closure", "CS", R.string.line_status_planned_closure, R.drawable.line_status_interruptions),
    SUSPENDED("Suspended", "SU", R.string.line_status_suspended, R.drawable.line_status_interruptions),
    //TODO check ids with the following statuses
    PART_SUSPENDED("Part suspended", "PS", R.string.line_status_part_suspended, R.drawable.line_status_interruptions),
    REDUCED_SERVICE("Reduced service", "RS", R.string.line_status_reduced_service, R.drawable.line_status_interruptions),
    BUS_SERVICE("Bus service", "BS", R.string.line_status_bus_service, R.drawable.line_status_interruptions);

    private final String mName;
    private final String mId;
    private final int mStatusResId;
    private final int mStatusIcon;

    private LineStatus(String name, String id, int statusResId, int statusIcon) {
        mName = name;
        mId = id;
        mStatusResId = statusResId;
        mStatusIcon = statusIcon;
    }

    public static LineStatus getLineStatusByName(String name) {
        for (LineStatus lineStatus : values()) {
            if (lineStatus.getName().equalsIgnoreCase(name)) {
                return lineStatus;
            }
        }
        return null;
        
    }
    
    public String getName() {
        return mName;
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
