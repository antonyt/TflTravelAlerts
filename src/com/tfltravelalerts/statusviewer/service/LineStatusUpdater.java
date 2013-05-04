
package com.tfltravelalerts.statusviewer.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.tfltravelalerts.common.networkstate.NetworkState;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusApiResult;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;

/**
 * Fetches the latest line statuses via TfL Line Status API.
 */
public class LineStatusUpdater {
    public static String LOG_TAG = "LineStatusUpdater";
    
    public static LineStatusApiResult update() {
        if(!NetworkState.isConnected()) {
            Log.i(LOG_TAG, "update: device is offline");
            NetworkState.broadcastWhenConnected(new LineStatusUpdateRequest());
            return new LineStatusApiResult(HttpStatus.SC_PRECONDITION_FAILED, null);
        }
        
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("android");
        HttpGet request = new HttpGet("http://cloud.tfl.gov.uk/TrackerNet/LineStatus");
        int statusCode = -1;
        try {
            HttpResponse response = httpClient.execute(request);
            statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                InputStream input = response.getEntity().getContent();
                List<LineStatusUpdate> lineStatusUpdates = LineStatusParser.parse(input);
                LineStatusUpdateSet lineStatusUpdateSet = new LineStatusUpdateSet(new Date(),
                        lineStatusUpdates);

                return new LineStatusApiResult(200, lineStatusUpdateSet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

        return new LineStatusApiResult(statusCode, null);
    }

}
