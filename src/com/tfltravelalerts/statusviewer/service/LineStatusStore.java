
package com.tfltravelalerts.statusviewer.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.common.persistence.ImmutableListDeserializer;
import com.tfltravelalerts.common.persistence.ImmutableSetDeserializer;
import com.tfltravelalerts.model.LineStatusUpdateSet;

/**
 * Persists a {@link LineStatusUpdateSet}. TODO: use a db?
 */
public class LineStatusStore {

    private static final String SHARED_PREFERENCE_NAME = "LineStatusStore";
    private static final String LINE_STATUS_UPDATE_SET_KEY = "LineStatusUpdateSet";

    public static void save(LineStatusUpdateSet lineStatusUpdateSet) {
        SharedPreferences preferences = getSharedPreferences();
        String json = new Gson().toJson(lineStatusUpdateSet);
        preferences.edit().putString(LINE_STATUS_UPDATE_SET_KEY, json).commit();

    }

    public static LineStatusUpdateSet load() {
        SharedPreferences preferences = getSharedPreferences();
        String json = preferences.getString(LINE_STATUS_UPDATE_SET_KEY, null);
        if (json != null) {
            return new GsonBuilder()
                    .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                    .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                    .create()
                    .fromJson(json, LineStatusUpdateSet.class);
        }

        return null;
    }

    private static SharedPreferences getSharedPreferences() {
        return TflApplication.getLastInstance().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

}
