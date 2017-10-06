
package com.tfltravelalerts.alerts.service;

import com.google.common.base.Strings;
import com.tfltravelalerts.model.LineStatusAlert;

public class AlertValidator {

    public static AlertValidationResult validateAlert(LineStatusAlert alert) {
        if (Strings.isNullOrEmpty(alert.getTitle())) {
            return AlertValidationResult.NO_TITLE;
        }

        if (alert.getTime() == null) {
            return AlertValidationResult.NO_TIME;
        }

        if (alert.getDays().isEmpty()) {
            return AlertValidationResult.NO_DAYS;
        }

        if (alert.getLines().isEmpty()) {
            return AlertValidationResult.NO_LINES;
        }

        return AlertValidationResult.SUCCESS;
    }
}
