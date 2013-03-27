
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class AlertDeletedEvent extends DataEvent<LineStatusAlert> {

    public AlertDeletedEvent(LineStatusAlert data) {
        super(data);
    }

}
