
package com.tfltravelalerts.common;

import org.holoeverywhere.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.InjectView;
import butterknife.Views;

import com.google.analytics.tracking.android.EasyTracker;
import com.tfltravelalerts.BuildConfig;
import com.tfltravelalerts.R;
import com.tfltravelalerts.analytics.EventAnalytics;
import com.tfltravelalerts.navigationdrawer.AppScreen;
import com.tfltravelalerts.navigationdrawer.AppScreenUtil;
import com.tfltravelalerts.navigationdrawer.NavigationDrawerAdapter;

public class TflBaseActivity extends Activity {
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mActivityTitle;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer_content)
    ListView mDrawerList;

    @Override
    protected void onCreate(Bundle sSavedInstanceState) {
        super.onCreate(sSavedInstanceState);
        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onPostCreate(Bundle sSavedInstanceState) {
        super.onPostCreate(sSavedInstanceState);
        setupDrawer();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    @SuppressWarnings("unused")
    private void setupDrawer() {
        Views.inject(this);
        if (mDrawerLayout == null || mDrawerList == null) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException("Not ready to use navigation drawer!");
            }
            EventAnalytics.thisShouldNotHappen("Activity not ready for NavigationDrawer",
                    getClass().getName());
            return;
        }

        mActivityTitle = getTitle();
        mDrawerTitle = getString(R.string.app_name);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        final NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(this);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
                onNavigationDrawerItemSelected(adapter.getItem(position));
            }
        });
    }

    protected void onNavigationDrawerItemSelected(AppScreen item) {
        Intent intent = new Intent(this, item.activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppScreenUtil.addScreenInfoToIntent(intent, item.screen);
        startActivity(intent);
    }
}
