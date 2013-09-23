
package com.tfltravelalerts.alerts;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.R;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.common.eventbus.EventBusFragment;
import com.tfltravelalerts.model.LineStatusAlert;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

public class ViewAlertsFragment extends EventBusFragment {

    private ListView mListView;
    private View mEmptyView;

    private AlertsListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.alerts_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_alert) {
            Intent intent = new Intent(getActivity(), EditAlertActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.alert_viewer_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        setupListView();
    }

    private void findViews() {
        mListView = (ListView) getView().findViewById(R.id.alerts_list);
        mEmptyView = getView().findViewById(R.id.empty_view);
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
        mListView.setEmptyView(mEmptyView);
    }

    public void onEventMainThread(AlertsUpdatedEvent event) {
        List<LineStatusAlert> alerts = event.getData().getAlerts();
        mAdapter.setData(alerts);
    }

}
