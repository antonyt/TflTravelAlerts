
package com.tfltravelalerts.alerts.service;


import android.content.Context;
import android.content.SharedPreferences;

import com.tfltravelalerts.TflApplication;

public class AlertIdGenerator {

    private static final String SHARED_PREFERENCES_NAME = "AlertsId";

    private static final String LAST_ID_KEY = "lastId";

    public static synchronized int generateId() {
        SharedPreferences preferences = TflApplication.getLastInstance().getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        int lastId = preferences.getInt(LAST_ID_KEY, 0);
        int newId = lastId + 1;
        preferences.edit().putInt(LAST_ID_KEY, newId).commit();

        return newId;
    }

}
