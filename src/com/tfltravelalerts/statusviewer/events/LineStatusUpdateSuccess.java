
package com.tfltravelalerts.statusviewer.events;

import com.tfltravelalerts.common.DataEvent;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class LineStatusUpdateSuccess extends DataEvent<LineStatusUpdateSet> {

    public LineStatusUpdateSuccess(LineStatusUpdateSet data) {
        super(data);
    }

}