
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class DeleteAlertRequest extends DataEvent<LineStatusAlert> {

    public DeleteAlertRequest(LineStatusAlert data) {
        super(data);
    }

}
