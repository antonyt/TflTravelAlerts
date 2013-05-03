
package com.tfltravelalerts.weekend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatus;
import com.tfltravelalerts.model.LineStatusUpdate;

/**
 * Parse XML weekend line status updates into a list of {@link LineStatusUpdate}
 * .
 */
public class WeekendStatusParser {

    public static List<LineStatusUpdate> parse(InputStream inputStream) {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            List<LineStatusUpdate> lineStatusUpdates = new ArrayList<LineStatusUpdate>();

            Line line = null;
            LineStatus lineStatus = null;
            String description = null;
            boolean isLine = false;
            boolean isStatus = false;
            boolean isMessage = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("Line")) {
                        isLine = true;
                    } else if (isLine && tag.equals("Name")) {
                        String lineName = parser.nextText();
                        line = Line.getLineByName(lineName);
                        if (parser.getEventType() != XmlPullParser.END_TAG) {
                            parser.next();
                        }
                        eventType = parser.getEventType();
                        continue;
                    } else if (isLine && tag.equals("Status")) {
                        isStatus = true;
                    } else if (isLine && isStatus && tag.equals("Message")) {
                        isMessage = true;
                    } else if (isLine && isStatus && isMessage && tag.equals("Text")) {
                        description = parser.nextText();
                        if (parser.getEventType() != XmlPullParser.END_TAG) {
                            parser.next();
                        }
                        eventType = parser.getEventType();
                        continue;
                    } else if (isLine && isStatus && !isMessage && tag.equals("Text")) {
                        String statusText = parser.nextText();
                        lineStatus = LineStatus.getLineStatusByName(statusText);
                        Log.i("tags", "line = " + line + ", lineStatus = " + lineStatus);
                        if (parser.getEventType() != XmlPullParser.END_TAG) {
                            parser.next();
                        }
                        eventType = parser.getEventType();
                        continue;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    String tag = parser.getName();
                    if (tag.equals("Line")) {
                        LineStatusUpdate lineStatusUpdate = new LineStatusUpdate(line,
                                lineStatus, description);
                        lineStatusUpdates.add(lineStatusUpdate);
                        isLine = false;
                    } else if (isLine && tag.equals("Status")) {
                        isStatus = false;
                    } else if (isLine && isStatus && tag.equals("Message")) {
                        isMessage = false;
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
