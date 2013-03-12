
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class AddOrUpdateAlertRequest extends DataEvent<LineStatusAlert> {

    public AddOrUpdateAlertRequest(LineStatusAlert data) {
        super(data);
    }

}
