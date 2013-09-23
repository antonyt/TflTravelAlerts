
package com.tfltravelalerts.gcm;

import com.google.android.gcm.GCMRegistrar;

import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.analytics.EventAnalytics;
import com.tfltravelalerts.common.networkstate.NetworkState;
import com.tfltravelalerts.common.requests.BackendConnection;
import com.tfltravelalerts.common.requests.BackendConnectionResult;
import com.tfltravelalerts.notification.RegisterForPushNotificationsRequest;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import de.greenrobot.event.EventBus;

public class GCMHandleNotifier {
    static final String LOG_TAG = GCMHandleNotifier.class.getSimpleName();

    public GCMHandleNotifier() {
        EventBus.getDefault().registerSticky(this);
    }

    public void onEventAsync(RegisterForPushNotificationsRequest obj) {
        Context context = TflApplication.getLastInstance();
        if (GCMRegistrar.isRegistered(context)) {
            notifyServer(GCMRegistrar.getRegistrationId(context), obj);
        } else {
            // we start listening first in case the response is immediate
            GCMRegistration.triggerOnRegistration(obj);
            registerWithGCM();
        }
    }

    static private void registerWithGCM() {
        Context context = TflApplication.getLastInstance();
        try {
            Log.d(LOG_TAG, "registering with GCM");
            GCMRegistrar.register(context, GCMRegistrationManager.SENDER_ID);
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, "Failed attempting to register with gcm", e);
            EventAnalytics.reportErrorCondition("failed attempting to register with GCM", e);
            // TODO show message
        }
    }

    /**
     * Make sure this method is always called from a background thread
     */
    static private void notifyServer(String gcmRegistrationHandle,
            RegisterForPushNotificationsRequest request) {
        if (NetworkState.isConnected()) {
            Log.i(LOG_TAG, "registering with server for alerts");
            BackendConnectionResult result = BackendConnection.post("register-for-alerts",
                    new BasicNameValuePair("gcm_handle", gcmRegistrationHandle),
                    new BasicNameValuePair("lines", request.getLinesString()));
            if(!result.isHttpStatusOk()) {
                result.logError(LOG_TAG, "failed to register for alerts");
                EventAnalytics.reportErrorCondition("failed to register for alerts", result.statusMessage);
                //TODO retry
            }
        } else {
            Log.i(LOG_TAG, "we are offline; delaying registration for alerts");
            NetworkState.broadcastWhenConnected(request);
        }
    }

}
