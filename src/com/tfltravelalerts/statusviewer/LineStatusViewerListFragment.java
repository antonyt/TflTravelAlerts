
package com.tfltravelalerts.statusviewer;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.R;
import com.tfltravelalerts.common.EventBusFragment;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

/**
 * Fragment to view summary status of every line.
 */
public class LineStatusViewerListFragment extends EventBusFragment {

    private ListView mListView;
    private View mRoot;
    private LineStatusListAdapter mAdapter;
    private View mRefreshIcon;

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

        MenuItem refresh = menu.findItem(R.id.refresh);
        setupRefreshIcon(refresh);
    }
    
    public void setupRefreshIcon(MenuItem refresh) {
        refresh.setActionView(R.layout.refresh_icon);
        mRefreshIcon = refresh.getActionView().findViewById(R.id.refresh_icon);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        mRefreshIcon.setTag(anim);
        mRefreshIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLineStatus();
            }
        });
    }

    private void updateLineStatus() {
        if(mRefreshIcon != null) {
            Animation anim = (Animation) mRefreshIcon.getTag();
            mRefreshIcon.startAnimation(anim);
        }

        Toast.makeText(getActivity(), "updating all lines", Toast.LENGTH_SHORT).show();
        getEventBus().postSticky(new LineStatusUpdateRequest());
    }

    public void onEventMainThread(LineStatusUpdateSuccess lineStatusUpdateEvent) {
        if(mRefreshIcon != null) {
            mRefreshIcon.clearAnimation();
        }

        LineStatusUpdateSet lineStatusUpdateSet = lineStatusUpdateEvent.getData();
        mAdapter.updateLineStatus(lineStatusUpdateSet.getLineStatusUpdates());

        if (lineStatusUpdateSet.isOldResult()) {
            Toast.makeText(getActivity(), "Old result - updating...", Toast.LENGTH_SHORT).show();
            updateLineStatus();
        }

    }

}
