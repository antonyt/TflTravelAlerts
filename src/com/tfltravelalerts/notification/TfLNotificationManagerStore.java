
package com.tfltravelalerts.notification;

import android.util.SparseArray;

import com.google.common.reflect.TypeToken;
import com.tfltravelalerts.common.persistence.SharedPreferencesStore;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class TfLNotificationManagerStore extends
        SharedPreferencesStore<SparseArray<LineStatusUpdateSet>> {

    private static final String NOTIFIED_UPDATES_KEY = "NotifiedUpdates";

    public TfLNotificationManagerStore() {
        super(new TypeToken<SparseArray<LineStatusUpdateSet>>() {
        }.getType(), NOTIFIED_UPDATES_KEY);
    }

    @Override
    protected int getCount(SparseArray<LineStatusUpdateSet> object) {
        if (object == null) {
            return -1;
        }
        return object.size();
    }
}
