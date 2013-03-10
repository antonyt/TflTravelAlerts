
package com.tfltravelalerts.statusviewer.events;

import com.tfltravelalerts.common.DataEvent;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class LineStatusSaveRequest extends DataEvent<LineStatusUpdateSet> {

    public LineStatusSaveRequest(LineStatusUpdateSet data) {
        super(data);
    }

}
