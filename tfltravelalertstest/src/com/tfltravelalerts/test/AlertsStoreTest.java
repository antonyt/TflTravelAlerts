
package com.tfltravelalerts.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.GsonBuilder;
import com.tfltravelalerts.common.persistence.ImmutableListDeserializer;
import com.tfltravelalerts.common.persistence.ImmutableSetDeserializer;
import com.tfltravelalerts.model.Day;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlertSet;
import com.tfltravelalerts.model.Time;

public class AlertsStoreTest extends TestCase {
    public void testSerialization() {
        List<LineStatusAlert> alerts = new ArrayList<LineStatusAlert>();
        LineStatusAlert alert = new LineStatusAlert.Builder(123).addDay(Day.MONDAY).addLine(
                Line.BAKERLOO).setTime(new Time(10, 15)).title("this is the title").build();
        alerts.add(alert);
        LineStatusAlertSet set = new LineStatusAlertSet(alerts);

        String json = new GsonBuilder()
                .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                .create()
                .toJson(set);

        assertEquals(
                "{\"mAlerts\":[{\"mDays\":[\"MONDAY\"],\"mTitle\":\"this is the title\",\"mLines\":[\"BAKERLOO\"],\"mTime\":{\"mHour\":10,\"mMinute\":15},\"mId\":123}]}",
                json);

    }

    public void testDeserialization() {
        List<LineStatusAlert> alerts = new ArrayList<LineStatusAlert>();
        LineStatusAlert alert = new LineStatusAlert.Builder(123).addDay(Day.MONDAY).addLine(
                Line.BAKERLOO).setTime(new Time(10, 15)).title("this is the title").build();
        alerts.add(alert);
        LineStatusAlertSet set = new LineStatusAlertSet(alerts);

        LineStatusAlertSet restored = new GsonBuilder()
                .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                .create()
                .fromJson(
                        "{\"mAlerts\":[{\"mDays\":[\"MONDAY\"],\"mTitle\":\"this is the title\",\"mLines\":[\"BAKERLOO\"],\"mTime\":{\"mHour\":10,\"mMinute\":15},\"mId\":123}]}",
                        LineStatusAlertSet.class);

        compareTwoAlertSets(set, restored);

    }

    private void compareTwoAlertSets(LineStatusAlertSet expected, LineStatusAlertSet restored) {
        assertEquals(restored.getAlerts(), expected.getAlerts());
        LineStatusAlert restoredAlert = restored.getAlerts().get(0);
        LineStatusAlert sampleAlert = expected.getAlerts().get(0);
        assertEquals(restoredAlert, sampleAlert);
        assertEquals(sampleAlert.getTitle(), restoredAlert.getTitle());
        assertEquals(sampleAlert.getDays(), restoredAlert.getDays());
        assertEquals(sampleAlert.getLines(), restoredAlert.getLines());
        assertEquals(sampleAlert.getTime(), restoredAlert.getTime());
        assertNotNull(restoredAlert.getTime());
    }

    public void testSerializationAndDeserialization() {
        List<LineStatusAlert> alerts = new ArrayList<LineStatusAlert>();
        LineStatusAlert alert = new LineStatusAlert.Builder(123).addDay(Day.MONDAY).addLine(
                Line.BAKERLOO).setTime(new Time(10, 15)).title("this is the title").build();
        alerts.add(alert);
        LineStatusAlertSet set = new LineStatusAlertSet(alerts);

        String json = new GsonBuilder()
                .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                .create()
                .toJson(set);

        LineStatusAlertSet restored = new GsonBuilder()
                .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                .create()
                .fromJson(json, LineStatusAlertSet.class);

        compareTwoAlertSets(set, restored);
    }
}
