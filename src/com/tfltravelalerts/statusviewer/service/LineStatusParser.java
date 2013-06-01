
package com.tfltravelalerts.statusviewer.service;

import com.google.gson.GsonBuilder;
import com.tfltravelalerts.analytics.ParsingAnalytics;
import com.tfltravelalerts.analytics.TflAnalytics;
import com.tfltravelalerts.model.LineStatusServerResponse;
import com.tfltravelalerts.model.LineStatusUpdate;

/**
 * Parse XML line status updates into a list of {@link LineStatusUpdate}.
 */
public class LineStatusParser {

    public static LineStatusServerResponse parse(String response) {
        ParsingAnalytics analytics = TflAnalytics.forParser("LineStatus");
        LineStatusServerResponse lineStatusUpdates = new GsonBuilder().create().fromJson(response,
                LineStatusServerResponse.class);
        analytics.done(lineStatusUpdates.size());
        return lineStatusUpdates;
    }
}
