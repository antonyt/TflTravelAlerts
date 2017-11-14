package com.tfltravelalerts.persistence

import com.tfltravelalerts.model.Line
import org.junit.Assert.assertEquals
import org.junit.Test

class LineTypeConverterTest {
    @Test
    fun testLinesConverterToInt() {
        assertEquals(0, LineTypeConverter.linesConverter(emptySet()))
        assertEquals(0b1, LineTypeConverter.linesConverter(setOf(Line.BAKERLOO)))
        assertEquals(0b100, LineTypeConverter.linesConverter(setOf(Line.CIRCLE)))
        assertEquals(0b10000000, LineTypeConverter.linesConverter(setOf(Line.NORTHERN)))
        assertEquals(0b1000000000, LineTypeConverter.linesConverter(setOf(Line.VICTORIA)))
        assertEquals(0b1000000000000, LineTypeConverter.linesConverter(setOf(Line.DLR)))
        assertEquals(0b100000000000000, LineTypeConverter.linesConverter(setOf(Line.TRAMS)))
    }

    @Test
    fun testLinesConverterFromIntSingle() {
        assertEquals(setOf<Line>(), LineTypeConverter.linesConverter(0))
        assertEquals(setOf(Line.BAKERLOO), LineTypeConverter.linesConverter(1))
        assertEquals(setOf(Line.CIRCLE), LineTypeConverter.linesConverter(4))
        assertEquals(setOf(Line.NORTHERN), LineTypeConverter.linesConverter(128))
        assertEquals(setOf(Line.VICTORIA), LineTypeConverter.linesConverter(512))
        assertEquals(setOf(Line.DLR), LineTypeConverter.linesConverter(4096))
        assertEquals(setOf(Line.TRAMS), LineTypeConverter.linesConverter(16384))
    }

    @Test
    fun testLinesConverterFromIntMulti() {
        assertEquals(setOf(Line.BAKERLOO, Line.CENTRAL, Line.NORTHERN), LineTypeConverter.linesConverter(131))
        assertEquals(setOf(Line.TRAMS, Line.DLR), LineTypeConverter.linesConverter(20480))
        assertEquals(Line.values().toSet(), LineTypeConverter.linesConverter(32767))
    }
}