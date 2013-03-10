
package com.tfltravelalerts.alerts;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.EditText;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.tfltravelalerts.R;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.common.eventbus.EventBusFragment;
import com.tfltravelalerts.model.LineStatusAlert;

public class EditAlertFragment extends EventBusFragment {

    public static final String ALERT_ID_KEY = "alertId";

    private int mAlertId;
    private LineStatusAlert mAlert;

    private View mRoot;
    private EditText mAlertTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveArgs();
    }

    private void retrieveArgs() {
        Bundle bundle = getArguments();
        mAlertId = bundle.getInt(ALERT_ID_KEY, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflateRootView(inflater, container);
        findViews();

        return mRoot;
    }

    private void inflateRootView(LayoutInflater inflater, ViewGroup container) {
        mRoot = inflater.inflate(R.layout.edit_alert_fragment, container, false);
    }

    private void findViews() {
        mAlertTitle = (EditText) mRoot.findViewById(R.id.alert_title);
    }

    private void updateTitle() {
        mAlertTitle.setText(mAlert.getTitle());
    }
    
    public void onEvent(AlertsUpdatedEvent event) {
        mAlert = event.getData().getAlertById(mAlertId);
        
        updateTitle();
    }
}
