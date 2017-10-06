package com.tfltravelalerts.alerts.service;

import com.tfltravelalerts.R;

public enum AlertValidationResult {
    NO_TITLE(R.string.edit_alert_validation_no_title),
    NO_TIME(R.string.edit_alert_validation_no_time),
    NO_DAYS(R.string.edit_alert_validation_no_days),
    NO_LINES(R.string.edit_alert_validation_no_lines),
    SUCCESS(0);

    private final int mMessageResId;

    private AlertValidationResult(int messageResId) {
        mMessageResId = messageResId;
    }
    
    public boolean isSucessResult() {
        return this == SUCCESS;
    }
    
    public int getMessageResId() {
        return mMessageResId;
    }
}