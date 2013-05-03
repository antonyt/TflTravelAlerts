
package com.tfltravelalerts.alerts;

import org.holoeverywhere.app.Fragment;

import android.os.Bundle;

import com.google.analytics.tracking.android.EasyTracker;
import com.tfltravelalerts.common.SinglePaneFragmentActivity;

public class EditAlertActivity extends SinglePaneFragmentActivity {

    public static final String ALERT_ID_KEY = EditAlertFragment.ALERT_ID_KEY;

    @Override
    protected Fragment buildFragment() {
        Fragment fragment = new EditAlertFragment();
        Bundle args = getIntent().getExtras();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }
    
    @Override
    protected void onStop() {
        EasyTracker.getInstance().activityStop(this);
        super.onStop();
    }
}
