
package com.tfltravelalerts.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.hash.Hashing;

public class Time implements Comparable<Time> {

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
        List<Time> sortedTimes = new ArrayList<Time>(times);
        Collections.sort(sortedTimes);

        return Joiner.on("\n").join(sortedTimes);
    }

    @Override
    public int compareTo(Time another) {
        int hourDiff = this.getHour() - another.getHour();
        int minDiff = this.getMinute() - another.getMinute();
        return hourDiff != 0 ? hourDiff : minDiff;
    }

}
