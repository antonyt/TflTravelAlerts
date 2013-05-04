
package com.tfltravelalerts.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tfltravelalerts.alerts.service.AlertIdGenerator;

public class LineStatusAlert {
    private static final String LOG_TAG = "LineStatusAlertUtil";
    private final static int LOOK_AHEAD_FOR_ALERT_TIME = 60; // unit: minutes
    private final static int LOOK_BEHIND_FOR_ALERT_TIME = -30; // unit: minutes
    
    private final int mId;
    private final String mTitle;
    private final ImmutableSet<Line> mLines;
    private final ImmutableSet<Day> mDays;
    private final Time mTime;
    private final boolean mOnlyNotifyForDisruptions;

    private LineStatusAlert(int id, String title, Set<Line> lines, Set<Day> days, Time time, boolean onlyNotifyForDisruptions) {
        mId = id;
        mTitle = title;
        mLines = ImmutableSet.copyOf(lines);
        mDays = ImmutableSet.copyOf(days);
        mTime = time;
        mOnlyNotifyForDisruptions = onlyNotifyForDisruptions;
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
    
    public boolean onlyNotifyForDisruptions() {
        return mOnlyNotifyForDisruptions;
    }
    
    @Override
    public String toString() {
        return "#"+getId()+" "+mTitle;
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
        private boolean mOnlyNotifyForDisruptions;

        public Builder(LineStatusAlert alert) {
            if (alert.getId() == -1) {
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
            if (id == -1) {
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
        
        public Builder setOnlyNotifyForDisruptions(boolean onlyNotifyForDisruptions) {
            mOnlyNotifyForDisruptions = onlyNotifyForDisruptions;
            return this;
        }

        public LineStatusAlert build() {
            return new LineStatusAlert(mId, mTitle, mLines, mDays, mTime, mOnlyNotifyForDisruptions);
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

    public long getNextAlertTime(long fromTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fromTime);

        while(calendar.get(Calendar.SECOND) != 0) {
            calendar.add(Calendar.SECOND, 1);
        }

        while(calendar.get(Calendar.MINUTE) != mTime.getMinute()) {
            calendar.add(Calendar.MINUTE, 1);
        }

        while(calendar.get(Calendar.HOUR_OF_DAY) != mTime.getHour()) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        Set<Integer> calendarDays = Sets.newHashSet();
        for(Day day : mDays) {
            calendarDays.add(day.getCalendarDay());
        }
        if(calendarDays.size() > 0) {
            //if we don't do this check we would go into an infinite loop
            while(!calendarDays.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
                calendar.add(Calendar.DATE, 1);
            }
        } else {
            Log.e("LineStatusAlert", "getNextAlertTime: there are no days in alert "+toString());
        }

        return calendar.getTimeInMillis();
    }
    
    public boolean isActive(DayTime now) {
        return alertActiveForTime(this, now);
    }
    
    public static boolean alertActiveForTime(LineStatusAlert alert, DayTime queryTime) {
        Time time = alert.getTime();
        if(time == null) {
            Log.w(LOG_TAG, "alertActiveForTime: alert time is null; returning false");
            return false;
        }
        DayTime alertTime = new DayTime(null, time);
        for (Day day : alert.getDays()) {
            alertTime.setDay(day);
            int timeToAlert = queryTime.differenceTo(alertTime);
            if (timeToAlert >= LOOK_BEHIND_FOR_ALERT_TIME
                    && timeToAlert <= LOOK_AHEAD_FOR_ALERT_TIME) {
                return true;
            }
        }
        return false;
    }
}
