
package com.tfltravelalerts.alerts.service;

import java.util.ArrayList;
import java.util.Collections;

import com.tfltravelalerts.alerts.events.AddOrUpdateAlertRequest;
import com.tfltravelalerts.alerts.events.AlertDeletedEvent;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.alerts.events.DeleteAlertRequest;
import com.tfltravelalerts.alerts.events.LoadAlertsRequest;
import com.tfltravelalerts.alerts.events.ValidateAlertRequest;
import com.tfltravelalerts.alerts.events.ValidateAlertResult;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;

import de.greenrobot.event.EventBus;

public class AlertsManager {

    private final AlertsStore mAlertsStore = new AlertsStore();
    private LineStatusAlertSet mAlerts = new LineStatusAlertSet(new ArrayList<LineStatusAlert>());

    public AlertsManager() {
        EventBus bus = EventBus.getDefault();
        bus.registerSticky(this);
        bus.post(new LoadAlertsRequest());
    }

    public void onEventAsync(LoadAlertsRequest request) {
        LineStatusAlertSet alerts = mAlertsStore.load();
        if(alerts == null) {
            alerts = new LineStatusAlertSet(Collections.<LineStatusAlert> emptyList());
        }
        synchronized (this) {
            mAlerts = alerts;
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            EventBus.getDefault().postSticky(event);
        }
    }

    public void onEventAsync(DeleteAlertRequest request) {
        LineStatusAlert alert = request.getData();

        synchronized (this) {
            mAlerts = mAlerts.removeAlert(alert);
            mAlertsStore.save(mAlerts);
            
            AlertDeletedEvent deleteEvent = new AlertDeletedEvent(alert);
            EventBus.getDefault().postSticky(deleteEvent);
            AlertsUpdatedEvent updateEvent = new AlertsUpdatedEvent(mAlerts);
            EventBus.getDefault().postSticky(updateEvent);
        }
    }

    public void onEventAsync(AddOrUpdateAlertRequest request) {
        LineStatusAlert alert = request.getData();
        
        synchronized (this) {
            mAlerts = mAlerts.addOrUpdateAlert(alert);
            mAlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            EventBus.getDefault().postSticky(event);
        }
    }
    
    public void onEventAsync(ValidateAlertRequest request) {
        LineStatusAlert alert = request.getData();
        
        AlertValidationResult validationResult = AlertValidator.validateAlert(alert);
        ValidateAlertResult event = new ValidateAlertResult(alert, validationResult);
        EventBus.getDefault().post(event);
    }

}
