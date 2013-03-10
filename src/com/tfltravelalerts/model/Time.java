
package com.tfltravelalerts.model;

public class Time {

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
    
}
