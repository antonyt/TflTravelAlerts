
package com.tfltravelalerts.weekend.service;

import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusApiResult;
import com.tfltravelalerts.weekend.events.WeekendStatusLoadRequest;
import com.tfltravelalerts.weekend.events.WeekendStatusSaveRequest;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateFailure;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateRequest;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateSuccess;

import de.greenrobot.event.EventBus;

/**
 * Service to retrieve weekend line status.
 */
public class WeekendStatusManager {

    private final WeekendStatusStore mWeekendStatusStore = new WeekendStatusStore();

    public WeekendStatusManager() {
        getEventBus().registerSticky(this);
        getEventBus().post(new WeekendStatusLoadRequest());
    }

    private EventBus getEventBus() {
        return EventBus.getDefault();
    }

    public void onEventAsync(WeekendStatusLoadRequest request) {
        LineStatusUpdateSet lineStatusUpdateSet = mWeekendStatusStore.load();
        if (lineStatusUpdateSet != null) {
            WeekendStatusUpdateSuccess event = new WeekendStatusUpdateSuccess(lineStatusUpdateSet);
            getEventBus().postSticky(event);
        } else {
            getEventBus().post(new WeekendStatusUpdateRequest());
        }
    }

    public void onEventAsync(WeekendStatusSaveRequest request) {
        mWeekendStatusStore.save(request.getData());
    }

    public void onEventAsync(WeekendStatusUpdateRequest request) {
        LineStatusApiResult result = WeekendStatusUpdater.update();
        if (result.isSuccess()) {
            LineStatusUpdateSet data = result.getData();
            getEventBus().post(new WeekendStatusSaveRequest(data));

            WeekendStatusUpdateSuccess event = new WeekendStatusUpdateSuccess(data);
            getEventBus().removeStickyEvent(WeekendStatusUpdateFailure.class);
            getEventBus().postSticky(event);
        } else {
            WeekendStatusUpdateFailure event = new WeekendStatusUpdateFailure();
            getEventBus().postSticky(event);
        }
    }

}
