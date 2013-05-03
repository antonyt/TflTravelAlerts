
package com.tfltravelalerts.weekend.service;

import com.tfltravelalerts.common.persistence.SharedPreferencesStore;
import com.tfltravelalerts.model.LineStatusUpdateSet;

/**
 * Persists a {@link LineStatusUpdateSet}. TODO: use a db?
 */
public class WeekendStatusStore extends SharedPreferencesStore<LineStatusUpdateSet> {

    private static final String LINE_STATUS_UPDATE_SET_KEY = "WeekendStatusStore.LineStatusUpdateSet";

    public WeekendStatusStore() {
        super(LineStatusUpdateSet.class, LINE_STATUS_UPDATE_SET_KEY);
    }

}
