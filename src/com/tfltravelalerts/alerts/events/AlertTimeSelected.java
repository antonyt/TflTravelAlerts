package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.Time;

public class AlertTimeSelected extends DataEvent<Time> {
    
    public AlertTimeSelected(Time time) {
        super(time);
    }

}
