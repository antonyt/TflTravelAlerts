package com.tfltravelalerts.model


private val NEW_ALARM_ID = -1

data class ConfiguredAlarm(
        val time: Time,
        val days: Set<Day>,
        val lines: Set<Line>,
        val suppressNotifications: Boolean,
        val enabled: Boolean) {

    fun includesLine(line: Line) = line in lines
    fun includesDay(day: Day) = day in days

    fun withEnabledValue(enabled: Boolean) : ConfiguredAlarm {
        return ConfiguredAlarm(time, days, lines, suppressNotifications, enabled)
    }

    companion object {
        val alarms = mutableListOf<ConfiguredAlarm>(
                ConfiguredAlarm(
                        Time(9,15),
                        setOf(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                        setOf(Line.JUBILEE, Line.METROPOLITAN),
                        false,
                        true),
                ConfiguredAlarm(
                        Time(19,0),
                        setOf(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                        setOf(Line.JUBILEE, Line.METROPOLITAN),
                        false,
                        true),
                ConfiguredAlarm(
                        Time(12,30),
                        setOf(Day.SATURDAY, Day.SUNDAY),
                        setOf(Line.CENTRAL),
                        true,
                        true),
                ConfiguredAlarm(
                        Time(12,30),
                        setOf(Day.SATURDAY, Day.SUNDAY),
                        Line.values().toSet(),
                        true,
                        false),
                ConfiguredAlarm(
                        Time(18,30),
                        Day.values().toSet(),
                        Line.values().toSet(),
                        true,
                        false)
        )
    }
}
