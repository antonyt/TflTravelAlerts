
package com.tfltravelalerts.test;

import java.util.Calendar;

import junit.framework.TestCase;

import com.tfltravelalerts.model.Day;
import com.tfltravelalerts.model.DayTime;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.LineStatusAlert.Builder;
import com.tfltravelalerts.model.Time;

public class LineStatusAlertTest extends TestCase {

    private LineStatusAlert alertAtNoon;
    private LineStatusAlert alertAtMidnight;
    private LineStatusAlert alertForWeekUnderflow;

    @Override
    protected void setUp() throws Exception {
        alertAtNoon = buildDefaultLineStatusAlert(12, 0);
        alertAtMidnight = buildDefaultLineStatusAlert(0, 15);
        alertForWeekUnderflow = buildDefaultLineStatusAlert(0, 10);
        alertForWeekUnderflow = LineStatusAlert.builder(alertForWeekUnderflow)
                .clearDays()
                .addDay(Day.SUNDAY, Day.MONDAY)
                .build();
    }

    private LineStatusAlert buildDefaultLineStatusAlert(int hour, int minute) {
        Builder builder = new LineStatusAlert.Builder();
        builder.addDay(new Day[] {Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY});
        builder.addLine(Line.BAKERLOO);
        builder.setTime(new Time(hour, minute));
        builder.setOnlyNotifyForDisruptions(false);
        return builder.build();
    }

    private long getTime(Day day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        // 1st september 2013 is a sunday
        cal.set(2013, Calendar.SEPTEMBER, day.getCalendarDay(), hour, minute, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public void testLookAheadAndLookBehind() {
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 12, 00)));
        assertEquals(false, alertAtNoon.isActive(new DayTime(Day.TUESDAY, 12, 00)));

        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 12, 10)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 12, 20)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 12, 30)));
        assertEquals(false, alertAtNoon.isActive(new DayTime(Day.MONDAY, 12, 31)));

        assertEquals(false, alertAtNoon.isActive(new DayTime(Day.MONDAY, 10, 59)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 11, 00)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 11, 10)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 11, 20)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 11, 30)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 11, 40)));
        assertEquals(true,  alertAtNoon.isActive(new DayTime(Day.MONDAY, 11, 50)));
    }

    public void testNextAlertTimeSimpleCase() {
        // test when week underflows
        long parameter = getTime(Day.MONDAY, 10, 0);
        long expected = getTime(Day.MONDAY, 11, 0);
        long actual = alertAtNoon.getNextAlertTime(parameter);

        assertEquals(expected, actual);
    }

    public void testNextAlertTimeNextDay() {
        long parameter = getTime(Day.MONDAY, 13, 0);
        long expected  = getTime(Day.WEDNESDAY, 11, 0);
        long actual = alertAtNoon.getNextAlertTime(parameter);

        assertEquals(expected, actual);
    }

    public void testNextAlertTimeAlertInProgress() {
        // the monday alert is already in progress so we're expecting to see
        // the next one
        long parameter = getTime(Day.MONDAY, 12, 0);
        long expected  = getTime(Day.WEDNESDAY, 11, 0);
        long actual = alertAtNoon.getNextAlertTime(parameter);

        assertEquals(expected, actual);
    }

    public void testNextAlarmTimeWithUnderflowSimpleCase() {
        long parameter = getTime(Day.MONDAY, 1, 0);
        long expected  = getTime(Day.TUESDAY, 23, 15);
        long actual = alertAtMidnight.getNextAlertTime(parameter);

        assertEquals(expected, actual);
    }

    public void testNextAlarmTimeWithUnderflowAlertInProgress() {
        long parameter = getTime(Day.WEDNESDAY, 23, 30);
        long expected  = getTime(Day.THURSDAY, 23, 15);
        long actual = alertAtMidnight.getNextAlertTime(parameter);

        assertEquals(expected, actual);

        //test after midnight too
        parameter = getTime(Day.WEDNESDAY, 0, 10);
        actual = alertAtMidnight.getNextAlertTime(parameter);
        assertEquals(expected, actual);
        
        //test after the alert time
        parameter = getTime(Day.WEDNESDAY, 0, 20);
        actual = alertAtMidnight.getNextAlertTime(parameter);
        assertEquals(expected, actual);
    }

    public void testNextAlarmTimeForNextWeek() {
        long milisecondsInAWeek = 1000*60*60*24*7;
        long parameter = getTime(Day.FRIDAY, 1, 0);
        long expected  = getTime(Day.SUNDAY, 23, 15);
        long actual = alertAtMidnight.getNextAlertTime(parameter);
        expected += milisecondsInAWeek;
        assertEquals(expected, actual);
    }

    public void testNextAlarmTimeWithWeekUnderflow() {
        long parameter = getTime(Day.WEDNESDAY, 1, 0);
        long expected  = getTime(Day.SATURDAY, 23, 10);
        long actual = alertForWeekUnderflow.getNextAlertTime(parameter);
        assertEquals(expected, actual);
    }
}
