
package com.tfltravelalerts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.tfltravelalerts.gcm.GCMRegistrationManager;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;

import de.greenrobot.event.EventBus;

/**
 * Service to handle GCM registration and messaging callbacks.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";
    
    public GCMIntentService() {
        super(GCMRegistrationManager.SENDER_ID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.d(TAG, "onMessage");
        if(intent.hasExtra("refresh_current_status")) {
            if(intent.getBooleanExtra("refresh_current_status", false)) {
                Log.i(TAG, "onMessage: received refresh_current_status push notification");
                EventBus.getDefault().post(new LineStatusUpdateRequest());
            }
        }
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.w(TAG, "onError registering to GCM: "+errorId);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "onRegistered to GCM");
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.w(TAG, "onUnregistered from GCM");
    }
}
