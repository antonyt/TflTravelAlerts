
package com.tfltravelalerts.common.persistence;

import org.holoeverywhere.preference.PreferenceManager;

import android.content.SharedPreferences;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tfltravelalerts.TflApplication;

public class SharedPreferencesStore<T> implements Store<T> {

    private final Class<?> mDataType;
    private final String mSharedPreferenceKey;
    private Gson mGson;

    public SharedPreferencesStore(Class<T> dataType, String sharedPreferenceKey) {
        mDataType = dataType;
        mSharedPreferenceKey = sharedPreferenceKey;
    }

    @Override
    public void save(T object) {
        SharedPreferences preferences = getSharedPreferences();
        String json = getGson().toJson(object);
        preferences.edit().putString(mSharedPreferenceKey, json).commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T load() {
        SharedPreferences preferences = getSharedPreferences();
        String json = preferences.getString(mSharedPreferenceKey, null);
        if (json != null) {
            return (T) getGson().fromJson(json, mDataType);
        }

        return null;
    }

    private Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                    .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                    .create();
        }

        return mGson;
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(TflApplication.getLastInstance());
    }

}
