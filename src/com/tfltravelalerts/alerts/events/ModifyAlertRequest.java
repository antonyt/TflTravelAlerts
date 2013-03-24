
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class ModifyAlertRequest extends DataEvent<LineStatusAlert> {

    public ModifyAlertRequest(LineStatusAlert data) {
        super(data);
    }

}