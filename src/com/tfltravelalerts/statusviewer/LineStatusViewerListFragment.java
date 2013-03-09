
package com.tfltravelalerts.statusviewer;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.EventBusFragment;
import com.tfltravelalerts.R;
import com.tfltravelalerts.model.LineStatusUpdateEvent;
import com.tfltravelalerts.model.LineStatusUpdateRequest;

/**
 * Fragment to view summary status of every line.
 */
public class LineStatusViewerListFragment extends EventBusFragment {

    private ListView mListView;
    private View mRoot;
    private LineStatusListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflateRootView(inflater, container);
        findViews();

        setupListView();

        return mRoot;
    }

    private void inflateRootView(LayoutInflater inflater, ViewGroup container) {
        mRoot = inflater.inflate(R.layout.line_status_viewer_list_fragment, container, false);
    }

    private void findViews() {
        mListView = (ListView) mRoot.findViewById(R.id.line_status_viewer_list);
    }

    private void setupListView() {
        mAdapter = new LineStatusListAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Intent intent = new Intent(getActivity(), LineStatusViewerDetailActivity.class);
                int lineId = (int) id;
                intent.putExtra(LineStatusViewerDetailActivity.LINE_ID_ARGS_KEY, lineId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.line_status_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            updateLineStatus();
            return true;
        }

        return false;
    }

    private void updateLineStatus() {
        getEventBus().postSticky(new LineStatusUpdateRequest());
    }

    public void onEvent(LineStatusUpdateEvent lineStatusUpdateEvent) {
        mAdapter.updateLineStatus(lineStatusUpdateEvent.getLineStatusUpdates());
    }

}
