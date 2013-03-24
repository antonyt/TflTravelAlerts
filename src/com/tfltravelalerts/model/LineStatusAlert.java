
package com.tfltravelalerts.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tfltravelalerts.alerts.service.AlertIdGenerator;

public class LineStatusAlert {

    private final int mId;
    private final String mTitle;
    private final ImmutableSet<Line> mLines;
    private final ImmutableSet<Day> mDays;
    private final Time mTime;

    private LineStatusAlert(int id, String title, Set<Line> lines, Set<Day> days, Time time) {
        mId = id;
        mTitle = title;
        mLines = ImmutableSet.copyOf(lines);
        mDays = ImmutableSet.copyOf(days);
        mTime = time;
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

    public Time getTime() {
        return mTime;
    }

    public int getId() {
        return mId;
    }

    public static Builder builder(int id) {
        return new Builder(id);
    }

    public static Builder builder(LineStatusAlert alert) {
        return new Builder(alert);
    }
    
    public static class Builder {

        private int mId = -1;
        private String mTitle;
        private Set<Line> mLines = new HashSet<Line>();
        private Set<Day> mDays = new HashSet<Day>();
        private Time mTime;

        public Builder(LineStatusAlert alert) {
            if(alert.getId() == -1) {
                mId = AlertIdGenerator.generateId();
            } else {
                mId = alert.getId();
            }
            mTitle = alert.getTitle();
            mLines = Sets.newHashSet(alert.getLines());
            mDays = Sets.newHashSet(alert.getDays());
            mTime = alert.getTime();
        }

        public Builder(int id) {
            if(id == -1) {
                mId = AlertIdGenerator.generateId();
            } else {
                mId = id;
            }
        }
        
        public Builder() {
            mId = AlertIdGenerator.generateId();
        }

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder addLine(Line... line) {
            mLines.addAll(Arrays.asList(line));
            return this;
        }

        public Builder addLine(Collection<Line> lines) {
            mLines.addAll(lines);
            return this;
        }

        public Builder clearLines() {
            mLines.clear();
            return this;
        }

        public Builder addDays(Collection<Day> days) {
            mDays.addAll(days);
            return this;
        }
        
        public Builder addDay(Day... day) {
            mDays.addAll(Arrays.asList(day));
            return this;
        }

        public Builder clearDays() {
            mDays.clear();
            return this;
        }

        public Builder setTime(Time time) {
            mTime = time;
            return this;
        }

        public LineStatusAlert build() {
            return new LineStatusAlert(mId, mTitle, mLines, mDays, mTime);
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
