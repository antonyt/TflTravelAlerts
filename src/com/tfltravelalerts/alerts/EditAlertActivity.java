
package com.tfltravelalerts.alerts;

import org.holoeverywhere.app.Fragment;

import android.os.Bundle;

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

}
