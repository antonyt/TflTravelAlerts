
package com.tfltravelalerts.common;




import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.tfltravelalerts.navigationdrawer.AppScreen;
import com.tfltravelalerts.navigationdrawer.AppScreen.Screen;
import com.tfltravelalerts.navigationdrawer.AppScreenUtil;

public abstract class TflBaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle sSavedInstanceState) {
        super.onCreate(sSavedInstanceState);
        ViewServer.get(this).addWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    /**
     * Returns whether this activity wants to use the drawer indicator or not.
     * 
     * @return
     */
    abstract protected boolean useDrawerIndicator();
    
    /**
     * this method is first called in TflBaseActivity.onResume
     * You should return the currently visible Screen or null if such
     * screen is not in the enum
     * 
     * If the activity changes the screen (using fragments) you need to keep
     * track of these changes as this method may be called at other times
     */
    protected Screen getCurrentScreen() {
        return null;
    }

    protected void onNavigationDrawerItemSelected(AppScreen item) {
        Intent intent = new Intent(this, item.activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppScreenUtil.addScreenInfoToIntent(intent, item.screen);
        startActivity(intent);
    }
}
