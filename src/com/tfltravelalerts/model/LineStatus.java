
package com.tfltravelalerts.model;

/**
 * Possible status for a London Underground line.
 */
public enum LineStatus {
    // TODO: find out all possible statuses
    GOOD_SERVICE("GS"),
    MINOR_DELAYS("MD"),
    SEVERE_DELAYS("SD"),
    PLANNED_CLOSURE("CS");

    private String mId;

    private LineStatus(String id) {
        mId = id;
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
}
