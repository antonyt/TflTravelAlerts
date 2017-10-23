package com.tfltravelalerts.model

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

data class Time (val hour: Int, val minute: Int) {
    val calendar by lazy {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal
    }
}

enum class Day constructor(val shortName: String) {
    MONDAY("Mon"),
    TUESDAY("Tue"),
    WEDNESDAY("Wed"),
    THURSDAY("Thu"),
    FRIDAY("Fri"),
    SATURDAY("Sat"),
    SUNDAY("Sun");

    val firstLetter = shortName.substring(0, 1)
}

interface TimePrinter {
    fun print(time: Time) : String
}

val DATE_FORMAT_24H = "HH:mm"
val DATE_FORMAT_12H = "hh:mm a"

class AndroidTimePrinter(context: Context) : TimePrinter {
    val formatPattern = if (android.text.format.DateFormat.is24HourFormat(context)) DATE_FORMAT_24H else DATE_FORMAT_12H
    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat(formatPattern)
    override fun print(time: Time): String {
        return format.format(time.calendar.time)
    }
}

