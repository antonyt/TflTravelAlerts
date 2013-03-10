
package com.tfltravelalerts.statusviewer.service;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.os.IBinder;

import com.tfltravelalerts.common.EventBusService;
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
        // TODO: load initial values from db
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onEvent(LineStatusUpdateRequest request) {
        Toast.makeText(this, "updating all lines", Toast.LENGTH_SHORT).show();
        new LineStatusUpdateTask().execute();
    }

    public void onEvent(LineStatusApiResult result) {
        if (result.isSuccess()) {
            LineStatusUpdateSuccess event = new LineStatusUpdateSuccess(result.getData());
            getEventBus().removeStickyEvent(LineStatusUpdateFailure.class);
            getEventBus().postSticky(event);
        } else {
            LineStatusUpdateFailure event = new LineStatusUpdateFailure();
            getEventBus().removeStickyEvent(LineStatusUpdateSuccess.class);
            getEventBus().postSticky(event);
        }
    }

}
