
package com.tfltravelalerts.model;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class LineStatusAlert {

    private final int mId;
    private final String mTitle;
    private final ImmutableSet<Line> mLines;
    private final ImmutableSet<Day> mDays;
    private final ImmutableSet<Time> mTimes;

    private LineStatusAlert(int id, String title, Set<Line> lines, Set<Day> days, Set<Time> times) {
        mId = id;
        mTitle = title;
        mLines = ImmutableSet.copyOf(lines);
        mDays = ImmutableSet.copyOf(days);
        mTimes = ImmutableSet.copyOf(times);
    }

    public String getTitle() {
        return mTitle;
    }

    public ImmutableSet<Line> getLines() {
        return mLines;
    }

    public ImmutableSet<Day> getDays() {
        return mDays;
    }

    public ImmutableSet<Time> getTimes() {
        return mTimes;
    }

    public int getId() {
        return mId;
    }

    public static class Builder {

        private int mId = -1;
        private String mTitle;
        private Set<Line> mLines = new HashSet<Line>();
        private Set<Day> mDays = new HashSet<Day>();
        private Set<Time> mTimes = new HashSet<Time>();

        public Builder(int id) {
            mId = id;
        }

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder addLine(Line line) {
            mLines.add(line);
            return this;
        }

        public Builder addDay(Day day) {
            mDays.add(day);
            return this;
        }

        public Builder addTime(Time time) {
            mTimes.add(time);
            return this;
        }

        public LineStatusAlert build() {
            return new LineStatusAlert(mId, mTitle, mLines, mDays, mTimes);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LineStatusAlert) {
            LineStatusAlert otherAlert = (LineStatusAlert) o;
            return getId() == otherAlert.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
