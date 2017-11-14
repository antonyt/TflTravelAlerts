package com.tfltravelalerts.model

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel

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
        const val NEW_ALARM_ID = 0 // Room treats 0 as unset for primary keys
    }
}