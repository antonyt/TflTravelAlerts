
package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class DeleteAlertRequest extends DataEvent<LineStatusAlert> {

    public DeleteAlertRequest(LineStatusAlert data) {
        super(data);
    }

}
