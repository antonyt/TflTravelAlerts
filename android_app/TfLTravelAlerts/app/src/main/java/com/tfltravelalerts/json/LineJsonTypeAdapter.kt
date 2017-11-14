package com.tfltravelalerts.json

import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.tfltravelalerts.model.Line
import java.io.IOException
import java.util.*

class LineJsonTypeAdapter : TypeAdapter<Line>() {
    private val nameToConstant = HashMap<String, Line>()
    private val constantToName = HashMap<Line, String>()

    init {
        // there is a bug with TFL where they use "TRAMS" for live status but "TRAM" for
        // weekend status. Just hardcoding an "alias"
        nameToConstant.put("TRAMS", Line.TRAM)
        try {
            for (constant in Line::class.java.enumConstants) {
                var name = constant.name
                val annotation = Line::class.java.getField(name).getAnnotation(SerializedName::class.java)
                if (annotation != null) {
                    name = annotation.value
                    for (alternate in annotation.alternate) {
                        nameToConstant.put(alternate, constant)
                    }
                }
                nameToConstant.put(name, constant)
                constantToName.put(constant, name)
            }
        } catch (e: NoSuchFieldException) {
            throw AssertionError(e)
        }

    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Line? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        return nameToConstant[reader.nextString()]
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Line?) {
        out.value(if (value == null) null else constantToName[value])
    }
}