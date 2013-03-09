package com.tfltravelalerts.statusviewer;

import java.util.Date;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.os.IBinder;

import com.tfltravelalerts.EventBusService;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatus;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateEvent;
import com.tfltravelalerts.model.LineStatusUpdateRequest;

public class LineStatusService extends EventBusService {

    public static final String LINE_ID_KEY = "line";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        String description = "No service between Earls Court and Aldgate East due to planned engineering work. GOOD SERVICE on the rest of the line.";
        LineStatusUpdate lineStatusUpdate = new LineStatusUpdate(Line.DISTRICT,
                LineStatus.PART_CLOSURE, new Date(), description);
        LineStatusUpdateEvent event = new LineStatusUpdateEvent(new Date(), lineStatusUpdate);
        getEventBus().postSticky(event);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
    public void onEvent(LineStatusUpdateRequest request) {
        Toast.makeText(this, "updating all lines", Toast.LENGTH_SHORT).show();
        LineStatusUpdateEvent oldEvent = (LineStatusUpdateEvent) getEventBus().getStickyEvent(LineStatusUpdateEvent.class);
        getEventBus().postSticky(oldEvent);
        
    }
    
}
