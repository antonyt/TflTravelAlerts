
package com.tfltravelalerts.statusviewer;

import java.util.Date;
import java.util.List;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.os.IBinder;

import com.tfltravelalerts.ApiResult;
import com.tfltravelalerts.EventBusService;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateEvent;
import com.tfltravelalerts.model.LineStatusUpdateRequest;

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

    public void onEvent(ApiResult<List<LineStatusUpdate>> result) {
        if (result.isSuccess()) {
            LineStatusUpdateEvent event = new LineStatusUpdateEvent(new Date(), result.getData());
            getEventBus().postSticky(event);
        }
    }

}
