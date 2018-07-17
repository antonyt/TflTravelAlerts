package com.tfltravelalerts.persistence

import android.arch.persistence.room.TypeConverter
import com.tfltravelalerts.model.Day
import com.tfltravelalerts.model.Line

object DayTypeConverter {
    @TypeConverter
    fun daysConverter(days: Set<Day>): Int {
        val value = days.sumBy { (1 shl it.ordinal) }
        return value
    }

    @TypeConverter
    fun daysConverter(value: Int): Set<Day> {
        val set = mutableSetOf<Day>()
        for (day in Day.values()) {
            if (value and (1 shl day.ordinal) != 0) {
                set.add(day)
            }
        }
        return set
    }
}

object LineTypeConverter {
    @TypeConverter
    fun linesConverter(lines: Set<Line>): Int {
        var value = 0
        for (line in lines) {
            value += (1 shl line.ordinal)
        }
        return value
    }

    @TypeConverter
    fun linesConverter(value: Int): Set<Line> {
        val set = mutableSetOf<Line>()
        for (line in Line.values()) {
            if (value and (1 shl line.ordinal) != 0) {
                set.add(line)
            }
        }
        return set
    }
}
