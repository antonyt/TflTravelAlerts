
package com.tfltravelalerts.statusviewer;

import org.holoeverywhere.app.Fragment;

import android.os.Bundle;

import com.google.analytics.tracking.android.EasyTracker;
import com.tfltravelalerts.common.SinglePaneFragmentActivity;

/**
 * Activity to display status information for a single line.
 */
public class LineStatusViewerDetailActivity extends SinglePaneFragmentActivity {

    public static final String LINE_ID_ARGS_KEY = LineStatusViewerDetailFragment.LINE_ID_ARGS_KEY;

    @Override
    protected Fragment buildFragment() {
        Bundle args = getIntent().getExtras();
        if (!args.containsKey(LINE_ID_ARGS_KEY)) {
            throw new IllegalArgumentException("Line id is required!");
        }

        Fragment fragment = new LineStatusViewerDetailFragment();
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
