
package com.tfltravelalerts.statusviewer.events;

import com.tfltravelalerts.common.eventbus.DataEvent;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class LineStatusSaveRequest extends DataEvent<LineStatusUpdateSet> {

    public LineStatusSaveRequest(LineStatusUpdateSet data) {
        super(data);
    }

}
