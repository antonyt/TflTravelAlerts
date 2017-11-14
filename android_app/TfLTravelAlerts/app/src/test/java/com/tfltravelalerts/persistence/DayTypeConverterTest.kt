package com.tfltravelalerts.persistence

import com.tfltravelalerts.model.Day
import org.junit.Assert.assertEquals
import org.junit.Test

class DayTypeConverterTest {
    @Test
    fun testDaysConverterSingleDayToInt() {
        assertEquals(0, DayTypeConverter.daysConverter(emptySet()))
        assertEquals(0b01, DayTypeConverter.daysConverter(setOf(Day.MONDAY)))
        assertEquals(0b010, DayTypeConverter.daysConverter(setOf(Day.TUESDAY)))
        assertEquals(0b010000, DayTypeConverter.daysConverter(setOf(Day.FRIDAY)))
        assertEquals(0b01000000, DayTypeConverter.daysConverter(setOf(Day.SUNDAY)))
    }

    @Test
    fun testDaysConverterMultipleDaysToInt() {
        assertEquals(0, DayTypeConverter.daysConverter(emptySet()))
        assertEquals(0b011, DayTypeConverter.daysConverter(setOf(Day.MONDAY, Day.TUESDAY)))
        assertEquals(0b0010010, DayTypeConverter.daysConverter(setOf(Day.FRIDAY, Day.TUESDAY)))
        assertEquals(0b01000011, DayTypeConverter.daysConverter(setOf(Day.SUNDAY, Day.MONDAY, Day.TUESDAY)))
        assertEquals(0b01111111, DayTypeConverter.daysConverter(setOf(Day.SUNDAY, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY)))
    }

    @Test
    fun testDaysConverterFromIntSimple() {
        assertEquals(setOf(Day.MONDAY), DayTypeConverter.daysConverter(1))
        assertEquals(setOf(Day.TUESDAY), DayTypeConverter.daysConverter(2))
        assertEquals(setOf(Day.FRIDAY), DayTypeConverter.daysConverter(16))
        assertEquals(setOf(Day.SUNDAY), DayTypeConverter.daysConverter(64))
    }

    @Test
    fun testDaysConverterFromIntMulti() {
        assertEquals(setOf(Day.MONDAY, Day.TUESDAY), DayTypeConverter.daysConverter(3))
        assertEquals(setOf(Day.TUESDAY, Day.FRIDAY), DayTypeConverter.daysConverter(18))
        assertEquals(setOf(Day.SUNDAY, Day.MONDAY, Day.TUESDAY), DayTypeConverter.daysConverter(67))
        assertEquals(Day.values().toSet(), DayTypeConverter.daysConverter(127))
    }
}