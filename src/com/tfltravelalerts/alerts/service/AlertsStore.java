
package com.tfltravelalerts.alerts.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.model.LineStatusAlertSet;

public class AlertsStore {

    private static final String SHARED_PREFERENCE_NAME = "AlertsStore";
    private static final String ALERTS_KEY = "AlertsUpdateSet";

    public static void save(LineStatusAlertSet lineStatusUpdateSet) {
        SharedPreferences preferences = getSharedPreferences();
        String json = new Gson().toJson(lineStatusUpdateSet);
        preferences.edit().putString(ALERTS_KEY, json).commit();
    }

    public static LineStatusAlertSet load() {
        SharedPreferences preferences = getSharedPreferences();
        String json = preferences.getString(ALERTS_KEY, null);
        if (json != null) {
            return new Gson().fromJson(json, LineStatusAlertSet.class);
        }

        return null;
    }

    private static SharedPreferences getSharedPreferences() {
        return TflApplication.getLastInstance().getSharedPreferences(SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

}
