
package com.tfltravelalerts.alerts.service;

import com.tfltravelalerts.common.persistence.SharedPreferencesStore;
import com.tfltravelalerts.model.LineStatusAlertSet;

public class AlertsStore extends SharedPreferencesStore<LineStatusAlertSet> {

    private static final String ALERTS_KEY = "AlertsStore.AlertsUpdateSet";

    public AlertsStore() {
        super(LineStatusAlertSet.class, ALERTS_KEY);
    }

    @Override
    protected int getCount(LineStatusAlertSet object) {
        if (object == null || object.getAlerts() == null) {
            return -1;
        }
        return object.getAlerts().size();
    }

}
