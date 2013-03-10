
package com.tfltravelalerts.alerts;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tfltravelalerts.R;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.common.eventbus.EventBusFragment;
import com.tfltravelalerts.model.LineStatusAlert;

public class ViewAlertsFragment extends EventBusFragment {

    private View mRoot;
    private ListView mListView;
    private AlertsListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflateRootView(inflater, container);
        findViews();

        setupListView();

        return mRoot;
    }

    private void inflateRootView(LayoutInflater inflater, ViewGroup container) {
        mRoot = inflater.inflate(R.layout.alert_viewer_fragment, container, false);
    }

    private void findViews() {
        mListView = (ListView) mRoot.findViewById(R.id.alerts_list);
    }

    private void setupListView() {
        mAdapter = new AlertsListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditAlertActivity.class);
                int alertId = (int) id; 
                intent.putExtra(EditAlertActivity.ALERT_ID_KEY, alertId);
                startActivity(intent);
            }
        });
    }

    public void onEventMainThread(AlertsUpdatedEvent event) {
        List<LineStatusAlert> alerts = event.getData().getAlerts();
        mAdapter.updateAlerts(alerts);
    }

}
