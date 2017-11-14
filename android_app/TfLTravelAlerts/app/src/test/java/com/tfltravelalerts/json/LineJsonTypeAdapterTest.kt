package com.tfltravelalerts.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tfltravelalerts.model.Line
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LineJsonTypeAdapterTest {
    lateinit var gson: Gson

    @Before
    fun setup() {
        gson = GsonBuilder().registerTypeAdapter(Line::class.java, LineJsonTypeAdapter()).create()

    }

    @Test
    fun readTram() {
        val res = gson.fromJson("TRAM", Line::class.java)
        assertEquals("Failed to parse 'TRAM'", Line.TRAM, res)
    }

    @Test
    fun readTrams() {
        val res = gson.fromJson("TRAMS", Line::class.java)
        assertEquals("Failed to parse 'TRAMS'", Line.TRAM, res)
    }

    @Test
    fun writeTram() {
        val res = gson.toJson(Line.TRAM)
        assertEquals("Failed to serialise TRAM", "\"TRAM\"", res)
    }
}