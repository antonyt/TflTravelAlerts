
package com.tfltravelalerts.common;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

import com.tfltravelalerts.R;

public class DateStrings {
    static public String getElapsedTimeForStatus(Context c, long when) {
        return getElapsedTimeForStatus(c, when, System.currentTimeMillis());
    }

    static public String getElapsedTimeForStatus(Context c, long when, long now) {

        /**
         * just now
         * moments ago
         * 1-60 minutes ago
         * at HH:MM (if today or in the last X hours)
         * yesterday
         * on Wednesday,
         * on 10 May
         * on 10 May 2012
         */
        long elapsedTime = Math.abs(now - when);

        if (elapsedTime <= 5 * DateUtils.SECOND_IN_MILLIS) {
            return c.getString(R.string.time_just_now);
        }
        if (elapsedTime <= DateUtils.MINUTE_IN_MILLIS) {
            return c.getString(R.string.time_moments_ago);
        }
        if (elapsedTime <= DateUtils.HOUR_IN_MILLIS) {
            return DateUtils.getRelativeTimeSpanString(when, now,
                    DateUtils.MINUTE_IN_MILLIS).toString();
        }
        if (elapsedTime <= 6 * DateUtils.HOUR_IN_MILLIS || sameDay(now, when)) {
            return "at " + DateUtils.formatDateTime(c, when, DateUtils.FORMAT_SHOW_TIME);
        }
        String string = DateUtils.getRelativeTimeSpanString(when, now,
                DateUtils.DAY_IN_MILLIS, 0).toString();
        if (elapsedTime < DateUtils.WEEK_IN_MILLIS) {
            return string;
        }
        return "on " + string;
    }

    public static boolean sameDay(long timeMs1, long timeMs2) {
        Time time = new Time();
        time.set(timeMs1);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(timeMs2);
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }
}
