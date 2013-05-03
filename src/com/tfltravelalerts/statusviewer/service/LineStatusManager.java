
package com.tfltravelalerts.statusviewer.service;

import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusApiResult;
import com.tfltravelalerts.statusviewer.events.LineStatusLoadRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusSaveRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateFailure;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

import de.greenrobot.event.EventBus;

/**
 * Service to retrieve line status.
 */
public class LineStatusManager {

    private final LineStatusStore mLineStatusStore = new LineStatusStore();

    public LineStatusManager() {
        getEventBus().registerSticky(this);
        getEventBus().post(new LineStatusLoadRequest());
    }

    private EventBus getEventBus() {
        return EventBus.getDefault();
    }

    public void onEventAsync(LineStatusLoadRequest request) {
        LineStatusUpdateSet lineStatusUpdateSet = mLineStatusStore.load();
        if (lineStatusUpdateSet != null) {
            LineStatusUpdateSuccess event = new LineStatusUpdateSuccess(lineStatusUpdateSet);
            getEventBus().postSticky(event);
        } else {
            getEventBus().post(new LineStatusUpdateRequest());
        }
    }

    public void onEventAsync(LineStatusSaveRequest request) {
        mLineStatusStore.save(request.getData());
    }

    public void onEventAsync(LineStatusUpdateRequest request) {
        LineStatusApiResult result = LineStatusUpdater.update();
        if (result.isSuccess()) {
            LineStatusUpdateSet data = result.getData();
            getEventBus().post(new LineStatusSaveRequest(data));

            LineStatusUpdateSuccess event = new LineStatusUpdateSuccess(data);
            getEventBus().removeStickyEvent(LineStatusUpdateFailure.class);
            getEventBus().postSticky(event);
        } else {
            LineStatusUpdateFailure event = new LineStatusUpdateFailure();
            getEventBus().postSticky(event);
        }
    }

}
