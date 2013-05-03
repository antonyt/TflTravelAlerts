
package com.tfltravelalerts;

import org.holoeverywhere.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.tfltravelalerts.alerts.ViewAlertsFragment;
import com.tfltravelalerts.common.TflBaseActivity;
import com.tfltravelalerts.debug.ExceptionViewerActivity;
import com.tfltravelalerts.statusviewer.LineStatusViewerListFragment;
import com.viewpagerindicator.PageIndicator;

public class MainActivity extends TflBaseActivity {

    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        setupViewPager();
        setupActionBar();
    }

    private void setupViewPager() {
        // TODO: fixup
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new LineStatusViewerListFragment();
                    case 1:
                        return new ViewAlertsFragment();
                    default:
                        return null;
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Line Status";
                    case 1:
                        return "Alerts";
                    default:
                        return super.getPageTitle(position);
                }
            }
        });

        PageIndicator indicator = (PageIndicator) findViewById(R.id.view_pager_indicator);
        indicator.setViewPager(mViewPager);
    }

    private void setupActionBar() {
        getSupportActionBar().setHomeButtonEnabled(TflApplication.DEBUG);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, ExceptionViewerActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        EasyTracker.getInstance().activityStop(this);
        super.onStop();
    }
}
