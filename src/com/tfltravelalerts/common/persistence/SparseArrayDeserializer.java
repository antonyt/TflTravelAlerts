
package com.tfltravelalerts.common.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.util.SparseArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SparseArrayDeserializer implements JsonDeserializer<SparseArray<?>> {

    @Override
    public SparseArray<?> deserialize(JsonElement json, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        Type[] parameterizedTypes = ((ParameterizedType) type).getActualTypeArguments();
        Type parameterizedType = parameterizedTypes[0];

        JsonArray jsonKeys = json.getAsJsonObject().getAsJsonArray("keys");
        int[] keys = new int[jsonKeys.size()];
        for (int i = 0; i < jsonKeys.size(); i++) {
            JsonElement element = jsonKeys.get(i);
            keys[i] = element.getAsInt();
        }

        JsonArray jsonValues = json.getAsJsonObject().getAsJsonArray("values");
        Object[] deserializedValues = new Object[jsonValues.size()];
        for (int i = 0; i < jsonValues.size(); i++) {
            JsonElement element = jsonValues.get(i);
            JsonObject obj = element.getAsJsonObject();
            deserializedValues[i] = context.deserialize(obj, parameterizedType);
        }

        SparseArray<Object> sparseArray = new SparseArray<Object>();
        for (int i = 0; i < jsonValues.size(); i++) {
            int key = keys[i];
            Object value = deserializedValues[i];
            sparseArray.put(key, value);
        }

        return sparseArray;
    }
}
