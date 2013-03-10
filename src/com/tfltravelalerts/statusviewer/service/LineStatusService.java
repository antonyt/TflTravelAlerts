
package com.tfltravelalerts.statusviewer.service;

import android.content.Intent;
import android.os.IBinder;

import com.tfltravelalerts.common.EventBusService;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusApiResult;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateFailure;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

/**
 * Service to retrieve line status.
 */
public class LineStatusService extends EventBusService {

    public static final String LINE_ID_KEY = "line";

    @Override
    public void onCreate() {
        super.onCreate();
        loadInitialValues();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private void loadInitialValues() {
        LineStatusUpdateSet lineStatusUpdateSet = LineStatusStore.load();
        if(lineStatusUpdateSet != null) {
            LineStatusUpdateSuccess event = new LineStatusUpdateSuccess(lineStatusUpdateSet);
            getEventBus().postSticky(event);
        }
    }

    public void onEventBackgroundThread(LineStatusUpdateRequest request) {
        LineStatusApiResult result = LineStatusUpdater.update();
        if (result.isSuccess()) {
            LineStatusUpdateSet data = result.getData();
            LineStatusStore.save(data);
            
            LineStatusUpdateSuccess event = new LineStatusUpdateSuccess(data);
            getEventBus().removeStickyEvent(LineStatusUpdateFailure.class);
            getEventBus().postSticky(event);
        } else {
            LineStatusUpdateFailure event = new LineStatusUpdateFailure();
            getEventBus().removeStickyEvent(LineStatusUpdateSuccess.class);
            getEventBus().postSticky(event);
        }
    }

}
