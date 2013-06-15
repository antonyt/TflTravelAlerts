
package com.tfltravelalerts.navigationdrawer;

import android.content.Context;
import android.content.Intent;

import com.tfltravelalerts.MainActivity;
import com.tfltravelalerts.R;
import com.tfltravelalerts.alerts.EditAlertActivity;
import com.tfltravelalerts.analytics.EventAnalytics;
import com.tfltravelalerts.navigationdrawer.AppScreen.Screen;

public class AppScreenUtil {
    private static final String SCREEN_ORDINAL = "SCREEN_ORDINAL";

    public static AppScreen[] getInitialScreenArray(Context c) {
        return new AppScreen[] {
                new AppScreen(c, R.string.current_status_title, AppScreen.Screen.CURRENT_STATUS, MainActivity.class),
                new AppScreen(c, R.string.weekend_status_title, AppScreen.Screen.WEEKEND_STATUS, MainActivity.class),
                new AppScreen(c, R.string.alerts_list_title, AppScreen.Screen.LIST_OF_ALERTS, MainActivity.class),
                new AppScreen(c, R.string.create_alert_title, AppScreen.Screen.CREATE_ALERT, EditAlertActivity.class),
        };
    }
    
    public static void addScreenInfoToIntent(Intent intent, Screen screen) {
        intent.putExtra(SCREEN_ORDINAL, screen.ordinal());
    }
    
    public static boolean hasScreenInfo(Intent intent) {
        return intent.hasExtra(SCREEN_ORDINAL);
    }
    
    public static Screen getScreenInfo(Intent intent) {
        int ordinal = intent.getIntExtra(SCREEN_ORDINAL, -1);
        if(ordinal >= 0) {
            return Screen.values()[ordinal];
        }
        EventAnalytics.thisShouldNotHappen("failed to get screen from intent");
        return null;
    }
}
