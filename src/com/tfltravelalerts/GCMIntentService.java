
package com.tfltravelalerts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * Service to handle GCM registration and messaging callbacks.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.d(TAG, "onMessage");
    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.d(TAG, "onError");
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.d(TAG, "onRegistered");
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.d(TAG, "onUnregistered");
    }
}
