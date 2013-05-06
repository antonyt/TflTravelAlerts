
package com.tfltravelalerts.common.persistence;

import java.lang.reflect.Type;

import android.util.SparseArray;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SparseArraySerializer implements JsonSerializer<SparseArray<?>> {

    @Override
    public JsonElement serialize(SparseArray<?> src, Type typeOfSrc,
            JsonSerializationContext context) {

        int size = src.size();
        int[] keys = new int[size];
        Object[] values = new Object[size];

        for (int i = 0; i < size; i++) {
            keys[i] = src.keyAt(i);
            values[i] = src.valueAt(i);
        }

        JsonObject root = new JsonObject();
        root.add("keys", context.serialize(keys));
        root.add("values", context.serialize(values));

        return root;
    }
}
