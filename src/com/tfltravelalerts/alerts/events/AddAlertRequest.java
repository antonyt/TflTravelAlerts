package com.tfltravelalerts.alerts.events;

import com.tfltravelalerts.common.DataEvent;
import com.tfltravelalerts.model.LineStatusAlert;

public class AddAlertRequest extends DataEvent<LineStatusAlert>{

    public AddAlertRequest(LineStatusAlert data) {
        super(data);
    }

}
