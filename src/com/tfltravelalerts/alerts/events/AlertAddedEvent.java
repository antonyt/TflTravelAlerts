
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class AlertAddedEvent extends DataEvent<LineStatusAlert> {

    public AlertAddedEvent(LineStatusAlert data) {
        super(data);
    }

}
