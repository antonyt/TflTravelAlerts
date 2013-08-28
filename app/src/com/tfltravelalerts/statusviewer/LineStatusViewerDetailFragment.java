
package com.tfltravelalerts.statusviewer;

import org.holoeverywhere.LayoutInflater;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;

import com.tfltravelalerts.R;
import com.tfltravelalerts.common.eventbus.EventBusFragment;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;
import com.tfltravelalerts.weekend.events.WeekendStatusUpdateSuccess;

/**
 * Fragment to view the detailed status of a single line.
 */
public class LineStatusViewerDetailFragment extends EventBusFragment {

    public static String LINE_ID_ARGS_KEY = "line";
    public static String IS_WEEKEND_ARGS_KEY = "isWeekend";

    private View mRoot;
    @InjectView(R.id.line_status_viewer_detail_title) TextView mTitle;
    @InjectView(R.id.line_status_viewer_detail_description) TextView mDescription;

    private boolean mIsWeekend;
    private Line mLine;
    private LineStatusUpdate mLineStatusUpdate;

    public static LineStatusViewerDetailFragment newInstance(int lineId, boolean isWeekend) {
        LineStatusViewerDetailFragment fragment = new LineStatusViewerDetailFragment();

        Bundle args = new Bundle();
        args.putInt(LINE_ID_ARGS_KEY, lineId);
        args.putBoolean(IS_WEEKEND_ARGS_KEY, isWeekend);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveArgs();
    }

    private void retrieveArgs() {
        Bundle args = getArguments();
        mLine = Line.getLineById(args.getInt(LINE_ID_ARGS_KEY));
        mIsWeekend = args.getBoolean(IS_WEEKEND_ARGS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflateRootView(inflater, container);
        Views.inject(this, mRoot);
        updateBackground();
        setupTitle();

        return mRoot;
    }

    private void inflateRootView(LayoutInflater inflater, ViewGroup container) {
        mRoot = inflater.inflate(R.layout.line_status_viewer_detail_fragment, container, false);
    }

    private void updateBackground() {
        mRoot.setBackgroundResource(mLine.getColorResId());
    }

    private void setupTitle() {
        mTitle.setText(mLine.getNameResId());
    }

    public void updateStatus() {
        if (mLineStatusUpdate != null) {
            mDescription.setText(mLineStatusUpdate.getDescription());
        } else {
            mDescription.setText(R.string.line_status_detail_no_description);
        }
    }

    public void onEventMainThread(LineStatusUpdateSuccess lineStatusUpdateEvent) {
        if (!mIsWeekend) {
            LineStatusUpdateSet lineStatusUpdateSet = lineStatusUpdateEvent.getData();
            mLineStatusUpdate = lineStatusUpdateSet.getUpdateForLine(mLine);
            updateStatus();
        }
    }

    public void onEventMainThread(WeekendStatusUpdateSuccess lineStatusUpdateEvent) {
        if (mIsWeekend) {
            LineStatusUpdateSet lineStatusUpdateSet = lineStatusUpdateEvent.getData();
            mLineStatusUpdate = lineStatusUpdateSet.getUpdateForLine(mLine);
            updateStatus();
        }
    }

}
