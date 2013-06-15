package com.tfltravelalerts.common;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.R;
import com.tfltravelalerts.common.eventbus.EventBusFragment;
import com.tfltravelalerts.statusviewer.LineStatusListAdapter;


public abstract class AbstractLineStatusFragment extends EventBusFragment {

    protected TextView mLastUpdateTime;
    protected TextView mTitle;
    protected ListView mListView;
    protected LineStatusListAdapter mAdapter;
    protected View mRoot;
    protected View mRefreshIcon;

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
        setupViewPagerIndicator();
        return mRoot;
    }

    abstract protected void setupListView();

    protected void setupViewPagerIndicator() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Typeface lightTypeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
            mTitle.setTypeface(lightTypeface);
        }
    }

    private void inflateRootView(LayoutInflater inflater, ViewGroup container) {
        mRoot = inflater.inflate(R.layout.line_status_viewer_list_fragment, container, false);
    }

    private void findViews() {
        mListView = (ListView) mRoot.findViewById(R.id.status_viewer_list);
        mTitle = (TextView)mRoot.findViewById(R.id.page_title);
        mLastUpdateTime = (TextView) mRoot.findViewById(R.id.update_time);
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

    abstract protected void updateLineStatus();    
}
