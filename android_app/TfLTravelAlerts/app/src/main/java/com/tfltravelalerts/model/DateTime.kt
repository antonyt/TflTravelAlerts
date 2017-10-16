package com.tfltravelalerts.model

import java.text.DateFormat
import java.util.*


data class Time (val hour: Int, val minute: Int) {
    val formattedString by lazy {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.time)
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
