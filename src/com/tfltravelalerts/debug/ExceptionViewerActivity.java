
package com.tfltravelalerts.debug;

import org.holoeverywhere.app.Fragment;

import com.tfltravelalerts.common.SinglePaneFragmentActivity;

public class ExceptionViewerActivity extends SinglePaneFragmentActivity {

    @Override
    protected Fragment buildFragment() {
        return new ExceptionViewerFragment();
    }

}
