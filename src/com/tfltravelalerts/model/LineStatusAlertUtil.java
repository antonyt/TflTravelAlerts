package com.tfltravelalerts.model;

import android.util.Log;


public class LineStatusAlertUtil {
	private final static int LOOK_AHEAD_FOR_ALERT_TIME = 60; // unit: minutes
	private final static int LOOK_BEHIND_FOR_ALERT_TIME = -30; // unit: minutes
	public static boolean alertActiveForTime(LineStatusAlert alert, DayTime queryTime) {
		Time time = alert.getTime();
		if(time == null) {
			Log.w("LineStatusAlertUtil", "alertActiveForTime: alert time is null; returning false");
			return false;
		}
		DayTime alertTime = new DayTime(null, time);
		for (Day day : alert.getDays()) {
			alertTime.setDay(day);
			int timeToAlert = queryTime.differenceTo(alertTime);
			if(timeToAlert <= LOOK_AHEAD_FOR_ALERT_TIME && timeToAlert >= LOOK_BEHIND_FOR_ALERT_TIME) {
				return true;
			}
		}
		return false;
	}
}
