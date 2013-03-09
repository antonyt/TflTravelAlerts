
package com.tfltravelalerts.statusviewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import com.tfltravelalerts.ApiResult;
import com.tfltravelalerts.model.LineStatusUpdate;

import de.greenrobot.event.EventBus;

/**
 * Task to fetch the latest line statuses via TfL Line Status API.
 *
 */
public class LineStatusUpdateTask extends AsyncTask<Void, Void, ApiResult<List<LineStatusUpdate>>> {

    @Override
    protected ApiResult<List<LineStatusUpdate>> doInBackground(Void... params) {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("android");
        HttpGet request = new HttpGet("http://cloud.tfl.gov.uk/TrackerNet/LineStatus");
        int statusCode = -1;
        try {
            HttpResponse response = httpClient.execute(request);
            statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                InputStream input = response.getEntity().getContent();
                List<LineStatusUpdate> lineStatusUpdates = LineStatusParser.parse(input);

                return new ApiResult<List<LineStatusUpdate>>(200, lineStatusUpdates);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

        return new ApiResult<List<LineStatusUpdate>>(statusCode, null);
    }

    @Override
    protected void onPostExecute(ApiResult<List<LineStatusUpdate>> result) {
        EventBus.getDefault().post(result);
    }

}
