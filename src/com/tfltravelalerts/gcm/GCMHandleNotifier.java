
package com.tfltravelalerts.gcm;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.tfltravelalerts.TflApplication;
import com.tfltravelalerts.common.networkstate.NetworkState;
import com.tfltravelalerts.common.requests.BackendConnection;
import com.tfltravelalerts.common.requests.BackendConnectionResult;
import com.tfltravelalerts.notification.RegisterForPushNotificationsRequest;

import de.greenrobot.event.EventBus;

public class GCMHandleNotifier {
    static final String LOG_TAG = GCMHandleNotifier.class.getSimpleName();
    boolean registeredWithEventBus = false;

    public GCMHandleNotifier() {
        EventBus.getDefault().registerSticky(this);
    }

    public void onEventAsync(RegisterForPushNotificationsRequest obj) {
        Context context = TflApplication.getLastInstance();
        if (GCMRegistrar.isRegistered(context)) {
            notifyServer(GCMRegistrar.getRegistrationId(context), obj);
        } else {
            // we start listening first in case the response is immediate
            listenForRegistration(obj);
            registerWithGCM();
        }
    }

    static private void registerWithGCM() {
        Context context = TflApplication.getLastInstance();
        try {
            Log.d(LOG_TAG, "registering with GCM");
            GCMRegistrar.register(context, GCMRegistrationManager.SENDER_ID);
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, "Failed to register with gcm", e);
            // TODO show message? analytics?
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
            result.close();
            if(!result.isHttpStatusOk()) {
                result.logError(LOG_TAG, "failed to register for alerts");
                //TODO retry; analytics? 
            }
        } else {
            Log.i(LOG_TAG, "we are offline; delaying registration for alerts");
            NetworkState.broadcastWhenConnected(request);
        }
    }

    private void listenForRegistration(RegisterForPushNotificationsRequest request) {
        if (!registeredWithEventBus) {
            synchronized (this) {
                if (!registeredWithEventBus) {
                    Log.d(LOG_TAG, "adding self as listener");
                    registeredWithEventBus = true;
                    EventBus.getDefault().register(this);
                }
            }
        }
        GCMRegistration.triggerOnRegistration(request);
    }
}
