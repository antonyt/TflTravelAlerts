
package com.tfltravelalerts.alerts.service;

import java.util.ArrayList;

import android.content.Intent;
import android.os.IBinder;

import com.google.common.collect.ImmutableList;
import com.tfltravelalerts.alerts.events.AddAlertRequest;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.alerts.events.DeleteAlertRequest;
import com.tfltravelalerts.alerts.events.LoadAlertsRequest;
import com.tfltravelalerts.alerts.events.ModifyAlertRequest;
import com.tfltravelalerts.common.eventbus.EventBusService;
import com.tfltravelalerts.model.Day;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;
import com.tfltravelalerts.model.Time;

public class AlertsService extends EventBusService {

    private LineStatusAlertSet mAlerts = new LineStatusAlertSet(new ArrayList<LineStatusAlert>());

    @Override
    public void onCreate() {
        super.onCreate();
        getEventBus().post(new LoadAlertsRequest());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onEventAsync(LoadAlertsRequest request) {
        // TODO: implement
        LineStatusAlertSet alerts = AlertsStore.load();
        if (alerts == null) {
            alerts = new LineStatusAlertSet(
                    ImmutableList.<LineStatusAlert> of(
                            LineStatusAlert.builder(AlertIdGenerator.generateId())
                                    .title("Daily commute")
                                    .addDay(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY,
                                            Day.FRIDAY)
                                    .addLine(Line.NORTHERN)
                                    .addTime(new Time(8, 0))
                                    .addTime(new Time(18, 30))
                                    .build(),

                            LineStatusAlert.builder(AlertIdGenerator.generateId())
                                    .title("French cooking class")
                                    .addDay(Day.THURSDAY, Day.SUNDAY)
                                    .addLine(Line.CENTRAL, Line.BAKERLOO)
                                    .addTime(new Time(20, 30))
                                    .addTime(new Time(21, 45))
                                    .build()
                            )
                    );
        }

        synchronized (this) {
            mAlerts = alerts;
            AlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
        }

    }

    public void onEventAsync(AddAlertRequest request) {
        LineStatusAlert alert = request.getData();

        synchronized (this) {
            mAlerts = mAlerts.addAlert(alert);
            AlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
        }
    }

    public void onEventAsync(DeleteAlertRequest request) {
        LineStatusAlert alert = request.getData();

        synchronized (this) {
            mAlerts = mAlerts.removeAlert(alert);
            AlertsStore.save(mAlerts);
            AlertsUpdatedEvent event = new AlertsUpdatedEvent(mAlerts);
            getEventBus().postSticky(event);
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

}
