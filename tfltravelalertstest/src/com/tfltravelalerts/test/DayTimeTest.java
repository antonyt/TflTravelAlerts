package com.tfltravelalerts.test;

import junit.framework.TestCase;

import com.tfltravelalerts.model.Day;
import com.tfltravelalerts.model.DayTime;
import com.tfltravelalerts.model.Time;

public class DayTimeTest extends TestCase {
	final int MINUTES_IN_DAY = 1440;
	
	private final Time tenToEleven = new Time(10, 50);
	private final Time tenToNoon = new Time(11, 50);
	private final Time noon = new Time(12, 00);
	private final Time tenPastNoon = new Time(12, 10);
	private final Time tenToMidnight = new Time(23, 50);
	private final Time tenPastMidnight = new Time(00, 10);

	private final DayTime mondayTenToEleven = new DayTime(Day.MONDAY, tenToEleven);
	private final DayTime mondayTenToNoon = new DayTime(Day.MONDAY, tenToNoon);
	private final DayTime mondayNoon = new DayTime(Day.MONDAY, noon);
	private final DayTime mondayTenPastNoon = new DayTime(Day.MONDAY, tenPastNoon);
	private final DayTime mondayTenToMidnight = new DayTime(Day.MONDAY, tenToMidnight);
	private final DayTime mondayTenPastMidnight = new DayTime(Day.MONDAY, tenPastMidnight);
	
	private final DayTime tuesdayNoon = new DayTime(Day.TUESDAY, noon);
	private final DayTime wednesdayNoon = new DayTime(Day.WEDNESDAY, noon);
	private final DayTime thrusdayNoon = new DayTime(Day.THURSDAY, noon);
	private final DayTime fridayNoon = new DayTime(Day.FRIDAY, noon);
	private final DayTime saturdayNoon = new DayTime(Day.SATURDAY, noon);
	private final DayTime sundayNoon = new DayTime(Day.SUNDAY, noon);

	private final DayTime tenMinutesToTuesday  = new DayTime(Day.MONDAY, tenToMidnight);
	private final DayTime tenMinutesToWednesday= new DayTime(Day.TUESDAY, tenToMidnight);
	private final DayTime tenMinutesIntoMonday = new DayTime(Day.MONDAY, tenPastMidnight);
	private final DayTime tenMinutesIntoTuesday= new DayTime(Day.TUESDAY, tenPastMidnight);
	private final DayTime tenMinutesIntoSaturday= new DayTime(Day.SATURDAY, tenPastMidnight);
	public void testTimeCases() {

		DayTime startTime = new DayTime(Day.MONDAY, new Time(12, 00));
		assertEquals(mondayNoon.differenceTo(startTime), 0);
		
		assertEquals(mondayNoon.differenceTo(mondayTenPastNoon), 10);
		assertEquals(mondayNoon.differenceTo(mondayTenToNoon), -10);
		assertEquals(mondayTenPastNoon.differenceTo(mondayNoon), -10);
		assertEquals(mondayTenToNoon.differenceTo(mondayNoon), 10);
		assertEquals(mondayTenToEleven.differenceTo(mondayTenPastNoon), 80);
		assertEquals(mondayTenPastNoon.differenceTo(mondayTenToEleven), -80);
		

		assertEquals(mondayTenToMidnight.differenceTo(mondayTenPastMidnight), -1420);
		assertEquals(mondayTenPastMidnight.differenceTo(mondayTenToMidnight), 1420);
		
	}
	
	public void testDayCases() {
		assertEquals(mondayNoon.differenceTo(sundayNoon), -1*MINUTES_IN_DAY);
		assertEquals(sundayNoon.differenceTo(mondayNoon), MINUTES_IN_DAY);
		
		assertEquals(thrusdayNoon.differenceTo(wednesdayNoon), -1*MINUTES_IN_DAY);
		assertEquals(wednesdayNoon.differenceTo(thrusdayNoon), MINUTES_IN_DAY);
		
		assertEquals(tuesdayNoon.differenceTo(thrusdayNoon), 2*MINUTES_IN_DAY);
		assertEquals(thrusdayNoon.differenceTo(tuesdayNoon), -2*MINUTES_IN_DAY);

		assertEquals(tuesdayNoon.differenceTo(fridayNoon), 3*MINUTES_IN_DAY);
		assertEquals(fridayNoon.differenceTo(tuesdayNoon), -3*MINUTES_IN_DAY);

		assertEquals(tuesdayNoon.differenceTo(saturdayNoon), -3*MINUTES_IN_DAY);
		assertEquals(saturdayNoon.differenceTo(tuesdayNoon), 3*MINUTES_IN_DAY);
	}
	
	public void testDayTime() {
		assertEquals(tenMinutesToTuesday.differenceTo(tenMinutesIntoTuesday), 20);
		assertEquals(tenMinutesIntoTuesday.differenceTo(tenMinutesToTuesday), -20);

		assertEquals(tenMinutesToTuesday.differenceTo(tenMinutesIntoSaturday), 20-3*MINUTES_IN_DAY);
		assertEquals(tenMinutesIntoSaturday.differenceTo(tenMinutesToTuesday), -20+3*MINUTES_IN_DAY);

		assertEquals(tenMinutesToWednesday.differenceTo(tenMinutesIntoMonday), 20-2*MINUTES_IN_DAY);
		assertEquals(tenMinutesIntoMonday.differenceTo(tenMinutesToWednesday), -20+2*MINUTES_IN_DAY);
	}

}
