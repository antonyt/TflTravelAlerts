
package com.tfltravelalerts.weekend.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class WeekendStatusSaveRequest extends DataEvent<LineStatusUpdateSet> {

    public WeekendStatusSaveRequest(LineStatusUpdateSet data) {
        super(data);
    }

}
