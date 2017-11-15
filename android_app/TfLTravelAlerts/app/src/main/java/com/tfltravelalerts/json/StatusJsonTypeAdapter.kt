package com.tfltravelalerts.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.tfltravelalerts.model.Status

class StatusTypeAdapter : TypeAdapter<Status>() {
    override fun write(out: JsonWriter, value: Status?) {
        out.value(value?.name)
    }

    override fun read(reader: JsonReader): Status {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return Status.UNKNOWN
        }
        return Status.tryParse(reader.nextString())
    }
}