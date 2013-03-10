
package com.tfltravelalerts.alerts;

import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.common.EventBusFragment;

public class EditAlertFragment extends EventBusFragment {

    public static final String ALERT_ID_KEY = "alertId";

    public void onEvent(AlertsUpdatedEvent event) {
        
    }
}
