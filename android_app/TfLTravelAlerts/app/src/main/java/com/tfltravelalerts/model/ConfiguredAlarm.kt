package com.tfltravelalerts.model

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel


private val NEW_ALARM_ID = -1

@PaperParcel
data class ConfiguredAlarm(
        val id: Int,
        val time: Time,
        val days: Set<Day>,
        val lines: Set<Line>,
        val notifyGoodService: Boolean,
        val enabled: Boolean) : Parcelable {

    fun includesLine(line: Line) = line in lines
    fun includesDay(day: Day) = day in days

    fun withEnabledValue(enabled: Boolean): ConfiguredAlarm {
        return ConfiguredAlarm(id, time, days, lines, notifyGoodService, enabled)
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelConfiguredAlarm.writeToParcel(this, dest, flags)
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelConfiguredAlarm.CREATOR

        val alarms = mutableListOf<ConfiguredAlarm>(
                ConfiguredAlarm(
                        1,
                        Time(9, 15),
                        setOf(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                        setOf(Line.JUBILEE, Line.METROPOLITAN),
                        false,
                        true),
                ConfiguredAlarm(
                        2,
                        Time(19, 0),
                        setOf(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                        setOf(Line.JUBILEE, Line.METROPOLITAN),
                        false,
                        true),
                ConfiguredAlarm(
                        3,
                        Time(12, 30),
                        setOf(Day.SATURDAY, Day.SUNDAY),
                        setOf(Line.CENTRAL),
                        true,
                        true),
                ConfiguredAlarm(
                        4,
                        Time(12, 30),
                        setOf(Day.SATURDAY, Day.SUNDAY),
                        Line.values().toSet(),
                        true,
                        false),
                ConfiguredAlarm(
                        5,
                        Time(18, 30),
                        Day.values().toSet(),
                        Line.values().toSet(),
                        true,
                        false),
                ConfiguredAlarm(
                        6,
                        Time(9, 15),
                        setOf(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                        setOf(Line.JUBILEE, Line.METROPOLITAN),
                        false,
                        true),
                ConfiguredAlarm(
                        7,
                        Time(19, 0),
                        setOf(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY),
                        setOf(Line.JUBILEE, Line.METROPOLITAN),
                        false,
                        true),
                ConfiguredAlarm(
                        8,
                        Time(12, 30),
                        setOf(Day.SATURDAY, Day.SUNDAY),
                        setOf(Line.CENTRAL),
                        true,
                        true),
                ConfiguredAlarm(
                        9,
                        Time(12, 30),
                        setOf(Day.SATURDAY, Day.SUNDAY),
                        Line.values().toSet(),
                        true,
                        false),
                ConfiguredAlarm(
                        10,
                        Time(18, 30),
                        Day.values().toSet(),
                        Line.values().toSet(),
                        true,
                        false)
        )
    }
}
