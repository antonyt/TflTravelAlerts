
package com.tfltravelalerts.alerts.events;

public class AlertTriggerEvent {
    final private int alertId; 
    public AlertTriggerEvent(int alertId) {
        this.alertId = alertId;
    }
    
    public int getAlertId() {
        return alertId;
    }
}
