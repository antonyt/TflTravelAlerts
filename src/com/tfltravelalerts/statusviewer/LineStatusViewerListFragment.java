
package com.tfltravelalerts.statusviewer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
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
import com.tfltravelalerts.common.CheatSheet;
import com.tfltravelalerts.common.eventbus.EventBusFragment;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateFailure;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

/**
 * Fragment to view summary status of every line.
 */
public class LineStatusViewerListFragment extends EventBusFragment {

    private View mRoot;
    private TextView mLastUpdateTime;
    private ListView mListView;

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
        mListView = (ListView) mRoot.findViewById(R.id.status_viewer_list);
        mLastUpdateTime = (TextView) mRoot.findViewById(R.id.update_time);
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
        View actionView = refresh.getActionView();
        mRefreshIcon = actionView.findViewById(R.id.refresh_icon);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        mRefreshIcon.setTag(anim);
        actionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLineStatus();
            }
        });
        CheatSheet.setup(actionView, R.string.action_refresh);
    }

    private void updateLineStatus() {
        if (mRefreshIcon != null) {
            Animation anim = (Animation) mRefreshIcon.getTag();
            mRefreshIcon.startAnimation(anim);
        }

        Toast.makeText(getActivity(), "updating all lines", Toast.LENGTH_SHORT).show();
        getEventBus().postSticky(new LineStatusUpdateRequest());
    }
    
    private void updateTimestamp(Date date) {
        java.text.DateFormat dateFormatter = SimpleDateFormat.getInstance();
        String dateFormat = dateFormatter.format(date);
        String updateTime = getString(R.string.last_update_time, dateFormat);
        mLastUpdateTime.setText(updateTime);
    }

    public void onEventMainThread(LineStatusUpdateSuccess event) {
        if (mRefreshIcon != null) {
            mRefreshIcon.clearAnimation();
        }

        LineStatusUpdateSet lineStatusUpdateSet = event.getData();
        mAdapter.updateLineStatus(lineStatusUpdateSet.getLineStatusUpdates());
        updateTimestamp(lineStatusUpdateSet.getDate());

        if (lineStatusUpdateSet.isOldResult()) {
            Toast.makeText(getActivity(), "Old result - updating...", Toast.LENGTH_SHORT).show();
            updateLineStatus();
        }
    }

    public void onEventMainThread(LineStatusUpdateFailure event) {
        if (mRefreshIcon != null) {
            mRefreshIcon.clearAnimation();
        }

        Toast.makeText(getActivity(), "Update failure!", Toast.LENGTH_SHORT).show();
    }

}
