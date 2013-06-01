
package com.tfltravelalerts.statusviewer.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.GsonBuilder;
import com.tfltravelalerts.model.LineStatusServerResponse;
import com.tfltravelalerts.model.LineStatusUpdate;

/**
 * Parse XML line status updates into a list of {@link LineStatusUpdate}.
 */
public class LineStatusParser {

    public static LineStatusServerResponse parse(InputStream inputStream) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
            String response = CharStreams.toString(inputStreamReader);
            inputStreamReader.close();
            LineStatusServerResponse lineStatusUpdates = new GsonBuilder().create().fromJson(response, LineStatusServerResponse.class);

            return lineStatusUpdates;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
