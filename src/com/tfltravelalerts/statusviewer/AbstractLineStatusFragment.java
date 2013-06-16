
package com.tfltravelalerts.statusviewer;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.R;
import com.tfltravelalerts.common.CheatSheet;
import com.tfltravelalerts.common.eventbus.EventBusFragment;

public abstract class AbstractLineStatusFragment extends EventBusFragment {

    @InjectView(R.id.update_time) protected TextView mLastUpdateTime;
    @InjectView(R.id.page_title) protected TextView mTitle;
    @InjectView(R.id.status_viewer_list) protected ListView mListView;

    protected LineStatusListAdapter mAdapter;
    protected View mRefreshIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.line_status_viewer_list_fragment, container, false);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Views.reset(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Views.inject(this, getView());
        setupListView();
        setupViewPagerIndicator();
        
        PullToRefreshAttacher attacher = new PullToRefreshAttacher(getActivity(), mListView);
        attacher.setRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefreshStarted(View view) {
                updateLineStatus();
            }
        });
    }

    abstract protected void setupListView();

    protected void setupViewPagerIndicator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Typeface lightTypeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
            mTitle.setTypeface(lightTypeface);
        }
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
