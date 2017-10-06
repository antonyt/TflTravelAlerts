
package com.tfltravelalerts.common.persistence;

import java.lang.reflect.Type;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.SparseArray;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.analytics.LoadSharedPreferencesAnalytics;
import com.tfltravelalerts.analytics.SaveSharedPreferencesAnalytics;

public abstract class SharedPreferencesStore<T> implements Store<T> {

    private final Type mDataType;
    private final String mSharedPreferenceKey;
    private Gson mGson;

    public SharedPreferencesStore(Type dataType, String sharedPreferenceKey) {
        mDataType = dataType;
        mSharedPreferenceKey = sharedPreferenceKey;
    }

    @Override
    public void save(T object) {
        SaveSharedPreferencesAnalytics analytics = new SaveSharedPreferencesAnalytics(mSharedPreferenceKey);
        SharedPreferences preferences = getSharedPreferences();
        String json = getGson().toJson(object);
        analytics.serializedObject();
        preferences.edit().putString(mSharedPreferenceKey, json).commit();
        analytics.done(getCount(object));
    }

    @Override
    @SuppressWarnings("unchecked")
    public T load() {
        LoadSharedPreferencesAnalytics analytics = new LoadSharedPreferencesAnalytics(mSharedPreferenceKey);
        SharedPreferences preferences = getSharedPreferences();
        String json = preferences.getString(mSharedPreferenceKey, null);
        analytics.loadedFromPreferences();
        if (json != null) {
            T object = (T) getGson().fromJson(json, mDataType);
            analytics.done(getCount(object));
            return object;
        }

        return null;
    }

    private Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
                    .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                    .registerTypeAdapter(SparseArray.class, new SparseArrayDeserializer())
                    .registerTypeAdapter(SparseArray.class, new SparseArraySerializer())
                    .create();
        }

        return mGson;
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(TflApplication.getLastInstance());
    }
    
    protected abstract int getCount(T object);
}
