
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.alerts.service.AlertValidationResult;
import com.tfltravelalerts.model.LineStatusAlert;

public class ValidateAlertResult {

    private final LineStatusAlert mAlert;
    private final AlertValidationResult mValidationResult;

    public ValidateAlertResult(LineStatusAlert alert, AlertValidationResult validationResult) {
        mAlert = alert;
        mValidationResult = validationResult;
    }
    
    public LineStatusAlert getAlert() {
        return mAlert;
    }
    
    public AlertValidationResult getValidationResult() {
        return mValidationResult;
    }

}
