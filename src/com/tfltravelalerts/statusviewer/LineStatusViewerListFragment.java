
package com.tfltravelalerts.statusviewer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.holoeverywhere.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.R;
import com.tfltravelalerts.common.AbstractLineStatusFragment;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateFailure;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateRequest;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

/**
 * Fragment to view summary status of every line.
 */
public class LineStatusViewerListFragment extends AbstractLineStatusFragment {

    public void setupListView() {
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

    

    @Override
    protected void updateLineStatus() {
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

    @Override
    protected void setupViewPagerIndicator() {
        super.setupViewPagerIndicator();
        mTitle.setText(R.string.current_status_title);
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
