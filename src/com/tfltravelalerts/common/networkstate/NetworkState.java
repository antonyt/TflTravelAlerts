
package com.tfltravelalerts.common.networkstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tfltravelalerts.TflApplication;

import de.greenrobot.event.EventBus;

public class NetworkState {
    private static String LOG_TAG = "NetworkStateObserver";

    static private BroadcastReceiver sSystemReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "onReceive");
            if (isConnected(context)) {
                Log.i(LOG_TAG, "We are now connected");
                context.unregisterReceiver(sSystemReceiver);
                EventBus.getDefault().post(new ConnectivityRestored());
            }
        }
    };

    static public boolean isConnected() {
        return isConnected(TflApplication.getLastInstance());
    }

    static public boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    static public void notifyWhenConnected() {
        startBroadcastReceiver();
    }

    /**
     * When the device becomes online, the given object will be broadcasted via
     * EventBus to subscribers
     * 
     * @param eventToBroadcast
     */
    public static void broadcastWhenConnected(final Object eventToBroadcast) {
        startBroadcastReceiver();
        EventBus.getDefault().register(new Object() {
            @SuppressWarnings("unused")
            public void onEvent(ConnectivityRestored event) {
                EventBus bus = EventBus.getDefault();
                bus.post(eventToBroadcast);
                bus.unregister(this);
            }
        });
    }

    private static void startBroadcastReceiver() {
        Context context = TflApplication.getLastInstance();
        context.registerReceiver(sSystemReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
