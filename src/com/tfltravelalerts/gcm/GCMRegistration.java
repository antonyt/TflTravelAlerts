
package com.tfltravelalerts.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.android.gcm.GCMConstants;
import com.google.android.gcm.GCMRegistrar;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.analytics.EventAnalytics;

import de.greenrobot.event.EventBus;

final class GCMRegistration extends BroadcastReceiver {
    private Object requestToBroadcast;

    private GCMRegistration(Object objectToBroadcastOnSuccess) {
        this.requestToBroadcast = objectToBroadcastOnSuccess;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK.equals(intent.getAction())) {
            if (intent.hasExtra(GCMConstants.EXTRA_ERROR)) {
                String error = intent.getStringExtra(GCMConstants.EXTRA_ERROR);
                Log.w(GCMHandleNotifier.LOG_TAG,
                        "onReceive: there was a problem registering with GCM: " + error);
                EventAnalytics.reportErrorCondition("received error registering with GCM", error);
                // TODO show a notification?
            } else {
                String registrationId = GCMRegistrar.getRegistrationId(context);
                Log.i(GCMHandleNotifier.LOG_TAG, "onReceive: got a GCM handle: " + registrationId);
                EventBus.getDefault().post(requestToBroadcast);
            }
            context.unregisterReceiver(this);
        }
    }

    static public void triggerOnRegistration(Object objectToBroadcastOnSuccess) {
        Context context = TflApplication.getLastInstance();
        BroadcastReceiver broadcastReceiver = new GCMRegistration(
                objectToBroadcastOnSuccess);
        IntentFilter filter = new IntentFilter(GCMConstants.INTENT_FROM_GCM_REGISTRATION_CALLBACK);
        context.registerReceiver(broadcastReceiver, filter);
    }

}
