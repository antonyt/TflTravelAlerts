package com.tfltravelalerts.test;

import junit.framework.TestCase;

import com.tfltravelalerts.model.Day;

public class DayTest extends TestCase {

	public void testCases() {
		
		assertEquals(Day.MONDAY.daysBetween(Day.MONDAY), 0);
		
		assertEquals(Day.MONDAY.daysBetween(Day.SUNDAY), -1);
		assertEquals(Day.SUNDAY.daysBetween(Day.MONDAY), 1);
		
		assertEquals(Day.THURSDAY.daysBetween(Day.WEDNESDAY), -1);
		assertEquals(Day.WEDNESDAY.daysBetween(Day.THURSDAY), 1);
		
		assertEquals(Day.TUESDAY.daysBetween(Day.THURSDAY), 2);
		assertEquals(Day.THURSDAY.daysBetween(Day.TUESDAY), -2);

		assertEquals(Day.TUESDAY.daysBetween(Day.FRIDAY), 3);
		assertEquals(Day.FRIDAY.daysBetween(Day.TUESDAY), -3);

		assertEquals(Day.TUESDAY.daysBetween(Day.SATURDAY), -3);
		assertEquals(Day.SATURDAY.daysBetween(Day.TUESDAY), 3);
	}

}
