
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

    private final AlertsStore mAlertsStore = new AlertsStore();
    private LineStatusAlertSet mAlerts = new LineStatusAlertSet(new ArrayList<LineStatusAlert>());

    public AlertsManager() {
        getEventBus().registerSticky(this);
        getEventBus().post(new LoadAlertsRequest());
    }

    private EventBus getEventBus() {
        return EventBus.getDefault();
    }
    
    public void onEventAsync(LoadAlertsRequest request) {
        LineStatusAlertSet alerts = mAlertsStore.load();
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
            mAlertsStore.save(mAlerts);
            
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
            mAlertsStore.save(mAlerts);
            
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
            mAlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
        }
    }

    public void onEventAsync(AddOrUpdateAlertRequest request) {
        LineStatusAlert alert = request.getData();
        
        synchronized (this) {
            mAlerts = mAlerts.addOrUpdateAlert(alert);
            mAlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
        }
    }

}
