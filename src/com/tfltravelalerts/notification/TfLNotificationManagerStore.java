package com.tfltravelalerts.notification;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.common.persistence.ImmutableListDeserializer;
import com.tfltravelalerts.common.persistence.ImmutableSetDeserializer;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class TfLNotificationManagerStore {
    private static final String SHARED_PREFERENCE_NAME = "TfLNotificationManagerStore";
    private static final String NOTIFIED_UPDATES_KEY = "NotifiedUpdates";

    public static void saveNotifiedUpdates(SparseArray<LineStatusUpdateSet> notifiedUpdates) {
        SharedPreferences preferences = getSharedPreferences();
        String json = new Gson().toJson(notifiedUpdates);
        preferences.edit().putString(NOTIFIED_UPDATES_KEY, json).commit();
    }

    public static SparseArray<LineStatusUpdateSet> load() {
        SharedPreferences preferences = getSharedPreferences();
        String json = preferences.getString(NOTIFIED_UPDATES_KEY, null);
        if (json != null) {
            Type arrayType = new TypeToken<SparseArray<LineStatusUpdateSet>>() {}.getType();
            return new GsonBuilder()
                    .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                    .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                    .create()
                    .fromJson(json, arrayType);
        }

        return null;
    }

    private static SharedPreferences getSharedPreferences() {
        return TflApplication.getLastInstance().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }
}
