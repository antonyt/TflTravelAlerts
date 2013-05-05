package com.tfltravelalerts.model;


public class DayTime {
	private Day mDay;
	private Time mTime;

	public DayTime(Day day, Time time) {
		this.mDay = day;
		this.mTime = time;
	}
	
	public DayTime(Day day, int hour, int minute) {
	    this(day, new Time(hour, minute));
	}

	private Day getDay() {
		return mDay;
	}

	public Time getTime() {
		return mTime;
	}

	public void setDay(Day day) {
		this.mDay = day;
	}

	public void setTime(Time time) {
		this.mTime = time;
	}

	public int differenceTo(DayTime toTime) {
		int days = mDay.daysBetween(toTime.getDay());
		int minutes = mTime.differenceTo(toTime.getTime());
		return days * 24 * 60 + minutes;
	}

	@Override
	public String toString() {
		return mDay + " " + mTime;
	}

	public static DayTime now() {
		android.text.format.Time systemTime = new android.text.format.Time();
		systemTime.setToNow();

		// weekday in android starts with sunday whereas in Day it starts with
		// Monday so we need to convert it (+7 to force it to be positive)
		int weekDay = systemTime.weekDay;
		Day day = Day.allDays()[(weekDay - 1 + 7) % 7];
		Time time = new Time(systemTime.hour, systemTime.minute);
		return new DayTime(day, time);
	}
}
