
package com.tfltravelalerts.weekend.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class WeekendStatusUpdateSuccess extends DataEvent<LineStatusUpdateSet> {

    public WeekendStatusUpdateSuccess(LineStatusUpdateSet data) {
        super(data);
    }

}
