
package com.tfltravelalerts.model;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class LineStatusAlertSet {

    private final ImmutableList<LineStatusAlert> mAlerts;

    public LineStatusAlertSet(List<LineStatusAlert> alerts) {
        mAlerts = ImmutableList.copyOf(alerts);
    }

    public ImmutableList<LineStatusAlert> getAlerts() {
        return mAlerts;
    }

    public ImmutableList<LineStatusAlert> getActiveAlerts(DayTime now) {
        Builder<LineStatusAlert> builder = ImmutableList.<LineStatusAlert>builder();
        for(LineStatusAlert alert : mAlerts) {
            if(alert.isActive(now)) {
                builder.add(alert);
            }
        }
        return builder.build();
    }

    public LineStatusAlertSet addAlert(LineStatusAlert alert) {
        List<LineStatusAlert> alerts = new ArrayList<LineStatusAlert>(mAlerts);
        alerts.add(alert);
        return new LineStatusAlertSet(alerts);
    }

    public LineStatusAlertSet removeAlert(LineStatusAlert alert) {
        List<LineStatusAlert> alerts = new ArrayList<LineStatusAlert>(mAlerts);
        alerts.remove(alert);
        return new LineStatusAlertSet(alerts);
    }

    public LineStatusAlertSet updateAlert(LineStatusAlert alert) {
        List<LineStatusAlert> alerts = new ArrayList<LineStatusAlert>(mAlerts);
        alerts.set(alerts.indexOf(alert), alert);
        return new LineStatusAlertSet(alerts);
    }

    public LineStatusAlertSet addOrUpdateAlert(LineStatusAlert alert) {
        List<LineStatusAlert> alerts = new ArrayList<LineStatusAlert>(mAlerts);
        int index = alerts.indexOf(alert);
        if (index == -1) {
            alerts.add(alert);
        } else {
            alerts.set(index, alert);
        }
        return new LineStatusAlertSet(alerts);

    }

    public LineStatusAlert getAlertById(int id) {
        for (LineStatusAlert alert : mAlerts) {
            if (alert.getId() == id) {
                return alert;
            }
        }
        return null;
    }
}
