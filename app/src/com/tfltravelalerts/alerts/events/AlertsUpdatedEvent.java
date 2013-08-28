
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusAlertSet;

public class AlertsUpdatedEvent extends DataEvent<LineStatusAlertSet> {

    public AlertsUpdatedEvent(LineStatusAlertSet data) {
        super(data);
    }

}
