package com.tfltravelalerts.notification;

import com.tfltravelalerts.common.persistence.SharedPreferencesStore;
import com.tfltravelalerts.model.SetOfLineStatusUpdateSet;

public class TfLNotificationManagerStore extends SharedPreferencesStore< SetOfLineStatusUpdateSet > {
    private static final String NOTIFIED_UPDATES_KEY = "NotifiedUpdates";
    public TfLNotificationManagerStore() {
            super(SetOfLineStatusUpdateSet.class, NOTIFIED_UPDATES_KEY);
    }
}
