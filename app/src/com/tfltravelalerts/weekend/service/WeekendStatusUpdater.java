
package com.tfltravelalerts.weekend.service;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;

import android.util.Log;

import com.tfltravelalerts.common.networkstate.NetworkState;
import com.tfltravelalerts.common.requests.BackendConnection;
import com.tfltravelalerts.common.requests.BackendConnectionResult;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusApiResult;
import com.tfltravelalerts.statusviewer.service.LineStatusParser;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateRequest;

public class WeekendStatusUpdater {

    private static final String LOG_TAG = "WeekendStatusUpdater";

    public static LineStatusApiResult update() {
        if (!NetworkState.isConnected()) {
            Log.i(LOG_TAG, "update: device is offline");
            NetworkState.broadcastWhenConnected(new WeekendStatusUpdateRequest());
            return new LineStatusApiResult(HttpStatus.SC_PRECONDITION_FAILED, null);
        }

        BackendConnectionResult backendResult = BackendConnection.get("/get-weekend-status");

        if (backendResult.isHttpStatusOk()) {
            List<LineStatusUpdate> lineStatusUpdates = LineStatusParser
                    .parse(backendResult.content);
            LineStatusUpdateSet lineStatusUpdateSet = new LineStatusUpdateSet(new Date(),
                    lineStatusUpdates);
            return new LineStatusApiResult(200, lineStatusUpdateSet);
        } else {
            backendResult.logError(LOG_TAG, "get weekend status");
            return new LineStatusApiResult(backendResult.statusCode, null);
        }

    }

}
