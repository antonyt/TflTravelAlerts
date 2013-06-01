
package com.tfltravelalerts.alerts;

import org.holoeverywhere.app.Fragment;

import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.common.SinglePaneFragmentActivity;

public class EditAlertActivity extends SinglePaneFragmentActivity {

    public static final String ALERT_ID_KEY = EditAlertFragment.ALERT_ID_KEY;

    @Override
    protected void onCreate(Bundle sSavedInstanceState) {
        super.onCreate(sSavedInstanceState);
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected Fragment buildFragment() {
        Fragment fragment = new EditAlertFragment();
        Bundle args = getIntent().getExtras();
        fragment.setArguments(args);
        return fragment;
    }
}
