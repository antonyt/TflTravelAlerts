
package com.tfltravelalerts.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.tfltravelalerts.R;
import com.tfltravelalerts.TflApplication;

public enum Day {
    MONDAY(R.string.day_full_monday, R.string.day_short_monday, R.string.day_super_short_monday),

    TUESDAY(R.string.day_full_tuesday, R.string.day_short_tuesday, R.string.day_super_short_tuesday),

    WEDNESDAY(R.string.day_full_wednesday, R.string.day_short_wednesday,
            R.string.day_super_short_wednesday),

    THURSDAY(R.string.day_full_thursday, R.string.day_short_thursday,
            R.string.day_super_short_thursday),

    FRIDAY(R.string.day_full_friday, R.string.day_short_friday, R.string.day_super_short_friday),

    SATURDAY(R.string.day_full_saturday, R.string.day_short_saturday,
            R.string.day_super_short_saturday),

    SUNDAY(R.string.day_full_sunday, R.string.day_short_sunday, R.string.day_super_short_sunday);

    private int mNormalNameResId;
    private int mShortNameResId;
    private int mSuperShortNameResId;

    private Day(int normalNameResId, int shortNameResId, int superShortNameResId) {
        mNormalNameResId = normalNameResId;
        mShortNameResId = shortNameResId;
        mSuperShortNameResId = superShortNameResId;
    }

    public int getNormalNameResId() {
        return mNormalNameResId;
    }

    public int getShortNameResId() {
        return mShortNameResId;
    }

    public int getSuperShortNameResId() {
        return mSuperShortNameResId;
    }

    public static Day[] weekdays() {
        return new Day[] {
                MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
        };
    }

    public static Day[] weekends() {
        return new Day[] {
                SATURDAY, SUNDAY
        };
    }

    public static Day[] allDays() {
        return values();
    }

    public static String buildShortString(Collection<Day> days) {
        List<Day> sortedDays = new ArrayList<Day>(days);
        Collections.sort(sortedDays);
        
        final Context context = TflApplication.getLastInstance();
        Collection<String> strings = Collections2.transform(sortedDays, new Function<Day, String>() {
            @Override
            public String apply(Day input) {
                return context.getString(input.getShortNameResId());
            }
        });
        return Joiner.on(", ").join(strings);
    }
}
