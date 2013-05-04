
package com.tfltravelalerts.weekend.service;

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
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateRequest;

/**
 * Fetches the latest line statuses via TfL Line Status API.
 */
public class WeekendStatusUpdater {

    private static final String LOG_TAG = "WeekendStatusUpdater";

    public static LineStatusApiResult update() {
        if(!NetworkState.isConnected()) {
            Log.i(LOG_TAG, "update: device is offline");
            NetworkState.broadcastWhenConnected(new WeekendStatusUpdateRequest());
            return new LineStatusApiResult(HttpStatus.SC_PRECONDITION_FAILED, null);
        }
        
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("android");
        HttpGet request = new HttpGet(
                "http://www.tfl.gov.uk/tfl/businessandpartners/syndication/feed.aspx?email=tfltravelalerts@gmail.com&feedId=7");
        int statusCode = -1;
        try {
            HttpResponse response = httpClient.execute(request);
            statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                InputStream input = response.getEntity().getContent();
                List<LineStatusUpdate> lineStatusUpdates = WeekendStatusParser.parse(input);
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
