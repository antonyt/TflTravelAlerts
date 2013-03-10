
package com.tfltravelalerts.statusviewer.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatus;
import com.tfltravelalerts.model.LineStatusUpdate;

/**
 * Parse XML line status updates into a list of {@link LineStatusUpdate}.
 */
public class LineStatusParser {

    public static List<LineStatusUpdate> parse(InputStream inputStream) {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            List<LineStatusUpdate> lineStatusUpdates = new ArrayList<LineStatusUpdate>();

            Line line = null;
            LineStatus lineStatus = null;
            String description = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("LineStatus")) {
                        description = parser.getAttributeValue(null, "StatusDetails");
                    } else if (tag.equals("Line")) {
                        int lineId = Integer.parseInt(parser.getAttributeValue(null, "ID"));
                        line = Line.getLineById(lineId);
                    } else if (tag.equals("Status")) {
                        String statusId = parser.getAttributeValue(null, "ID");
                        lineStatus = LineStatus.getLineStatusById(statusId);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("LineStatus")) {
                        LineStatusUpdate lineStatusUpdate = new LineStatusUpdate(line,
                                lineStatus, description);
                        lineStatusUpdates.add(lineStatusUpdate);
                    }
                }
                eventType = parser.next();
            }

            return lineStatusUpdates;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
