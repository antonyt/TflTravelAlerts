package com.tfltravelalerts.test;

import junit.framework.TestCase;

import com.tfltravelalerts.model.Time;

public class TimeTest extends TestCase {
	private final Time tenToEleven = new Time(10, 50);
	private final Time tenToNoon = new Time(11, 50);
	private final Time noon = new Time(12, 00);
	private final Time tenPastNoon = new Time(12, 10);
	private final Time tenToMidnight = new Time(23, 50);
	private final Time tenPastMidnight = new Time(00, 10);

	public void testSameTimeReturnsZero() {
		Time startTime = new Time(12, 00);
		assertEquals(noon.differenceTo(startTime), 0);
	}

	public void testSimpleTimeDifferences() {
		
		assertEquals(noon.differenceTo(tenPastNoon), 10);
		assertEquals(noon.differenceTo(tenToNoon), -10);
		assertEquals(tenPastNoon.differenceTo(noon), -10);
		assertEquals(tenToNoon.differenceTo(noon), 10);
		assertEquals(tenToEleven.differenceTo(tenPastNoon), 80);
		assertEquals(tenPastNoon.differenceTo(tenToEleven), -80);
	}
	
	public void testDayWrapping() {
		assertEquals(tenToMidnight.differenceTo(tenPastMidnight), -1420);
		assertEquals(tenPastMidnight.differenceTo(tenToMidnight), 1420);
	}

}
