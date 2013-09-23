
package com.tfltravelalerts.statusviewer.service;

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
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;

public class LineStatusUpdater {
    public static String LOG_TAG = "LineStatusUpdater";

    public static LineStatusApiResult update() {
        if (!NetworkState.isConnected()) {
            Log.i(LOG_TAG, "update: device is offline");
            NetworkState.broadcastWhenConnected(new LineStatusUpdateRequest());
            return new LineStatusApiResult(HttpStatus.SC_PRECONDITION_FAILED, null);
        }

        BackendConnectionResult backendResult = BackendConnection.get("/get-line-status");

        if (backendResult.isHttpStatusOk()) {
            List<LineStatusUpdate> lineStatusUpdates = LineStatusParser
                    .parse(backendResult.content);
            LineStatusUpdateSet lineStatusUpdateSet = new LineStatusUpdateSet(new Date(),
                    lineStatusUpdates);
            return new LineStatusApiResult(200, lineStatusUpdateSet);
        } else {
            backendResult.logError(LOG_TAG, "get line status");
            return new LineStatusApiResult(backendResult.statusCode, null);
        }
    }

}
