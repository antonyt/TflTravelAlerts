
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class ValidateAlertRequest extends DataEvent<LineStatusAlert> {

    public ValidateAlertRequest(LineStatusAlert data) {
        super(data);
    }

}
