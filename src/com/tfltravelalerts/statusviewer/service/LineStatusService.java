
package com.tfltravelalerts.statusviewer.service;

import android.content.Intent;
import android.os.IBinder;

import com.tfltravelalerts.common.eventbus.EventBusService;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusApiResult;
import com.tfltravelalerts.statusviewer.events.LineStatusLoadRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusSaveRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateFailure;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

/**
 * Service to retrieve line status.
 */
public class LineStatusService extends EventBusService {

    @Override
    public void onCreate() {
        super.onCreate();
        getEventBus().post(new LineStatusLoadRequest());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    public void onEventAsync(LineStatusLoadRequest request) {
        LineStatusUpdateSet lineStatusUpdateSet = LineStatusStore.load();
        if(lineStatusUpdateSet != null) {
            LineStatusUpdateSuccess event = new LineStatusUpdateSuccess(lineStatusUpdateSet);
            getEventBus().postSticky(event);
        } else {
            getEventBus().post(new LineStatusUpdateRequest());
        }
    }
    
    public void onEventAsync(LineStatusSaveRequest request) {
        LineStatusStore.save(request.getData());
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
