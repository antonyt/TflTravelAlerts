
package com.tfltravelalerts.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class Time implements Comparable<Time> {

    private static final String TAG = Time.class.getName();

    private final int mHour;
    private final int mMinute;

    public Time(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
    }

    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Time) {
            Time other = (Time) o;
            return this.getHour() == other.getHour() && this.getMinute() == other.getMinute();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getHour(), getMinute());
    }

    @Override
    public String toString() {
        String hour = mHour < 10 ? "0" + String.valueOf(mHour) : String.valueOf(mHour);
        String minute = mMinute < 10 ? "0" + String.valueOf(mMinute) : String.valueOf(mMinute);

        return hour + ":" + minute;
    }

    public static String buildString(Collection<Time> times) {
        return buildString(times, "\n");
    }

    public static String buildString(Collection<Time> times, String separator) {
        List<Time> sortedTimes = new ArrayList<Time>(times);
        Collections.sort(sortedTimes);

        return Joiner.on(separator).join(sortedTimes);
    }

    /**
     * @param timeString 12:34 format
     * @return
     */
    public static Time parseTime(String timeString) {
        if (Strings.isNullOrEmpty(timeString)) {
            return null;
        }

        try {
            String[] parts = timeString.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            return new Time(hour, minute);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Cannot parse time - " + timeString, e);
            return null;
        }
    }

    @Override
    public int compareTo(Time another) {
        int hourDiff = this.getHour() - another.getHour();
        int minDiff = this.getMinute() - another.getMinute();
        return hourDiff != 0 ? hourDiff : minDiff;
    }

    public int differenceTo(Time another) {
        int hourDiff = another.getHour() - this.getHour();
        int minDiff = another.getMinute() - this.getMinute();
        return hourDiff * 60 + minDiff;
    }
}
