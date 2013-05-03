
package com.tfltravelalerts.weekend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;

import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusApiResult;

/**
 * Fetches the latest line statuses via TfL Line Status API.
 */
public class WeekendStatusUpdater {

    public static LineStatusApiResult update() {
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
