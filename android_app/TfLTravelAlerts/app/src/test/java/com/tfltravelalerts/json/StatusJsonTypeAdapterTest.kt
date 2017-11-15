package com.tfltravelalerts.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.LineStatus
import com.tfltravelalerts.model.Status
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StatusJsonTypeAdapterTest {
    lateinit var gson: Gson
    @Before
    fun setup() {
        gson = GsonBuilder()
                .registerTypeAdapter(Status::class.java, StatusJsonTypeAdapter())
                .create()
    }

    @Test
    fun testWriteValid() {
        val res = gson.toJson(Status.GOOD_SERVICE)
        assertEquals("\"GOOD_SERVICE\"", res)
    }

    @Test
    fun testReadValid() {
        val res = gson.fromJson("\"GOOD_SERVICE\"", Status::class.java)
        assertEquals(Status.GOOD_SERVICE, res)
    }

    @Test
    fun testReadUnknown() {
        val res = gson.fromJson("\"XYZ\"", Status::class.java)
        assertEquals(Status.UNKNOWN, res)
    }

    @Test
    fun testReadNull() {
        val res = gson.fromJson("null", Status::class.java)
        assertEquals(Status.UNKNOWN, res)
    }

    @Test
    fun testWithLineStatusWithoutStatus() {
        val json = """{"line":"JUBILEE", "details": "example"}"""
        val expected = LineStatus(Line.JUBILEE, Status.UNKNOWN, "example")
        val actual = gson.fromJson(json, LineStatus::class.java)
        assertEquals(expected, actual)
    }

    @Test
    fun testWithLineStatusWithOtherStatus() {
        val json = """{"line":"JUBILEE", "details": "example", "state": "XYZ"}"""
        val expected = LineStatus(Line.JUBILEE, Status.UNKNOWN, "example")
        val actual = gson.fromJson(json, LineStatus::class.java)
        assertEquals(expected, actual)
    }
}