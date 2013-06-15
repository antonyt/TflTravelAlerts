package com.tfltravelalerts.weekend;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.R;
import com.tfltravelalerts.common.AbstractLineStatusFragment;
import com.tfltravelalerts.common.CheatSheet;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.LineStatusListAdapter;
import com.tfltravelalerts.statusviewer.LineStatusViewerDetailActivity;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateFailure;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateRequest;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateSuccess;

public class WeekendStatusViewerListFragment extends AbstractLineStatusFragment {
    
    private static final String LOG_TAG = WeekendStatusViewerListFragment.class.getSimpleName();
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weekend_status_list_menu, menu);

        MenuItem refresh = menu.findItem(R.id.refresh);
        setupRefreshIcon(refresh);
    }

    @Override
    protected void setupViewPagerIndicator() {
        super.setupViewPagerIndicator();
        mTitle.setText(R.string.weekend_status_title);
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
    
    @Override
    protected void setupListView() {
        mAdapter = new LineStatusListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Intent intent = new Intent(getActivity(), LineStatusViewerDetailActivity.class);
                int lineId = (int) id;
                intent.putExtra(LineStatusViewerDetailActivity.LINE_ID_ARGS_KEY, lineId);
                intent.putExtra(LineStatusViewerDetailActivity.IS_WEEKEND_ARGS_KEY, true);
                startActivity(intent);
            }
        });
    }
    
    private void updateTimestamp(Date date) {
        java.text.DateFormat dateFormatter = SimpleDateFormat.getInstance();
        String dateFormat = dateFormatter.format(date);
        String updateTime = getString(R.string.last_update_time, dateFormat);
        mLastUpdateTime.setText(updateTime);
    }
    
    @Override
    protected void updateLineStatus() {
        if (mRefreshIcon != null) {
            Animation anim = (Animation) mRefreshIcon.getTag();
            mRefreshIcon.startAnimation(anim);
        }
        getEventBus().post(new WeekendStatusUpdateRequest());
    }
    
    public void onEventMainThread(WeekendStatusUpdateSuccess event) {
        Log.i(LOG_TAG, "onEvent WeekendStatusUpdateSuccess");
        
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
    
    public void onEventMainThread(WeekendStatusUpdateFailure event) {
        Log.i(LOG_TAG, "onEvent WeekendStatusUpdateFailure");
        
        if (mRefreshIcon != null) {
            mRefreshIcon.clearAnimation();
        }

        Toast.makeText(getActivity(), "Update failure!", Toast.LENGTH_SHORT).show();
    }
}
