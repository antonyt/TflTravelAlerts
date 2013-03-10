
package com.tfltravelalerts.statusviewer;

import org.holoeverywhere.LayoutInflater;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tfltravelalerts.R;
import com.tfltravelalerts.common.EventBusFragment;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusUpdate;
import com.tfltravelalerts.model.LineStatusUpdateSet;
import com.tfltravelalerts.statusviewer.events.LineStatusUpdateSuccess;

/**
 * Fragment to view the detailed status of a single line.
 */
public class LineStatusViewerDetailFragment extends EventBusFragment {

    public static String LINE_ID_ARGS_KEY = "line";

    private View mRoot;
    private TextView mTitle;
    private TextView mDescription;

    private Line mLine;
    private LineStatusUpdate mLineStatusUpdate;

    public static LineStatusViewerDetailFragment newInstance(int lineId) {
        LineStatusViewerDetailFragment fragment = new LineStatusViewerDetailFragment();

        Bundle args = new Bundle();
        args.putInt(LINE_ID_ARGS_KEY, lineId);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflateRootView(inflater, container);
        findViews();
        updateBackground();
        setupTitle();

        return mRoot;
    }

    private void inflateRootView(LayoutInflater inflater, ViewGroup container) {
        mRoot = inflater.inflate(R.layout.line_status_viewer_detail_fragment, container, false);
    }

    private void findViews() {
        mTitle = (TextView) mRoot.findViewById(R.id.line_status_viewer_detail_title);
        mDescription = (TextView) mRoot.findViewById(R.id.line_status_viewer_detail_description);
    }

    private void updateBackground() {
        mRoot.setBackgroundResource(mLine.getColorResId());
    }

    private void setupTitle() {
        mTitle.setText(mLine.getNameResId());
    }

    public void updateStatus() {
        if(mLineStatusUpdate != null) {
            mDescription.setText(mLineStatusUpdate.getDescription());
        } else {
            mDescription.setText("");
        }
    }

    public void onEventMainThread(LineStatusUpdateSuccess lineStatusUpdateEvent) {
        LineStatusUpdateSet lineStatusUpdateSet = lineStatusUpdateEvent.getLineStatusUpdateSet();
        mLineStatusUpdate = lineStatusUpdateSet.getUpdateForLine(mLine);
        updateStatus();
    }

}
