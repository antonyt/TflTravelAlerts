
package com.tfltravelalerts.common;

import org.holoeverywhere.app.Fragment;

import android.os.Bundle;

import com.tfltravelalerts.R;

/**
 * Base class for an activity that displays a single fragment.
 */
public abstract class SinglePaneFragmentActivity extends TflBaseActivity {

    @Override
    protected void onCreate(Bundle sSavedInstanceState) {
        super.onCreate(sSavedInstanceState);
        setContentView(R.layout.single_page_fragment_activity);
        if (sSavedInstanceState == null) {
            Fragment fragment = buildFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.single_pane_fragment_container, fragment).commit();
        }
    }

    protected abstract Fragment buildFragment();

    
}
