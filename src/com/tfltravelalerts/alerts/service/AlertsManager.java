
package com.tfltravelalerts.alerts.service;

import java.util.ArrayList;
import java.util.Collections;

import com.tfltravelalerts.alerts.events.AddAlertRequest;
import com.tfltravelalerts.alerts.events.AddOrUpdateAlertRequest;
import com.tfltravelalerts.alerts.events.AlertAddedEvent;
import com.tfltravelalerts.alerts.events.AlertDeletedEvent;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.alerts.events.DeleteAlertRequest;
import com.tfltravelalerts.alerts.events.LoadAlertsRequest;
import com.tfltravelalerts.alerts.events.ModifyAlertRequest;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;

import de.greenrobot.event.EventBus;

public class AlertsManager {

    private LineStatusAlertSet mAlerts = new LineStatusAlertSet(new ArrayList<LineStatusAlert>());

    public AlertsManager() {
        getEventBus().registerSticky(this);
        getEventBus().post(new LoadAlertsRequest());
    }

    private EventBus getEventBus() {
        return EventBus.getDefault();
    }
    
    public void onEventAsync(LoadAlertsRequest request) {
        LineStatusAlertSet alerts = AlertsStore.load();
        if(alerts == null) {
            alerts = new LineStatusAlertSet(Collections.<LineStatusAlert> emptyList());
        }
        synchronized (this) {
            mAlerts = alerts;
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
        }
    }

    public void onEventAsync(AddAlertRequest request) {
        LineStatusAlert alert = request.getData();

        synchronized (this) {
            mAlerts = mAlerts.addAlert(alert);
            AlertsStore.save(mAlerts);
            
            AlertAddedEvent addEvent = new AlertAddedEvent(alert);
            getEventBus().postSticky(addEvent);
            AlertsUpdatedEvent updateEvent = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(updateEvent);
        }
    }

    public void onEventAsync(DeleteAlertRequest request) {
        LineStatusAlert alert = request.getData();

        synchronized (this) {
            mAlerts = mAlerts.removeAlert(alert);
            AlertsStore.save(mAlerts);
            
            AlertDeletedEvent deleteEvent = new AlertDeletedEvent(alert);
            getEventBus().postSticky(deleteEvent);
            AlertsUpdatedEvent updateEvent = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(updateEvent);
        }
    }

    public void onEventAsync(ModifyAlertRequest request) {
        LineStatusAlert alert = request.getData();

        synchronized (this) {
            mAlerts = mAlerts.updateAlert(alert);
            AlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
        }
    }

    public void onEventAsync(AddOrUpdateAlertRequest request) {
        LineStatusAlert alert = request.getData();
        
        synchronized (this) {
            mAlerts = mAlerts.addOrUpdateAlert(alert);
            AlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
        }
    }

}
