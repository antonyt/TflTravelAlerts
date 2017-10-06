package com.tfltravelalerts.alerts.events;

public class AlertTimeoutEvent {
    final private int alertId;
    public AlertTimeoutEvent(int alertId) {
        this.alertId = alertId;
    }

    public int getAlertId() {
        return alertId;
    }
}
