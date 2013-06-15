
package com.tfltravelalerts;

import org.holoeverywhere.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.alerts.ViewAlertsFragment;
import com.tfltravelalerts.common.TflBaseActivity;
import com.tfltravelalerts.debug.ExceptionViewerActivity;
import com.tfltravelalerts.navigationdrawer.AppScreen;
import com.tfltravelalerts.navigationdrawer.AppScreen.Screen;
import com.tfltravelalerts.navigationdrawer.AppScreenUtil;
import com.tfltravelalerts.statusviewer.LineStatusViewerListFragment;
import com.tfltravelalerts.weekend.WeekendStatusViewerListFragment;

public class MainActivity extends TflBaseActivity {

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Views.inject(this);

        setupViewPager();
        setupActionBar();
        initViewPager(savedInstanceState);
    }

    private void initViewPager(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (AppScreenUtil.hasScreenInfo(intent)) {
            Screen screenInfo = AppScreenUtil.getScreenInfo(intent);
            switchToFragment(screenInfo);
        } else if (savedInstanceState == null) {
            mViewPager.setCurrentItem(1);
        }
    }

    private void setupViewPager() {
        // TODO: fixup
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new WeekendStatusViewerListFragment();
                    case 1:
                        return new LineStatusViewerListFragment();
                    case 2:
                        return new ViewAlertsFragment();
                    default:
                        return null;
                }
            }
        });

        int margin = getResources().getDimensionPixelSize(R.dimen.view_pager_page_margin);
        mViewPager.setPageMargin(margin);
    }

    private void setupActionBar() {
        getSupportActionBar().setHomeButtonEnabled(BuildConfig.DEBUG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, ExceptionViewerActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNavigationDrawerItemSelected(AppScreen item) {
        boolean consumed = switchToFragment(item.screen);
        // only call super if we haven't handled it ourselves
        if (!consumed) {
            super.onNavigationDrawerItemSelected(item);
        }
    }

    private boolean switchToFragment(Screen screen) {
        boolean consumed = true;
        switch (screen) {
            case CURRENT_STATUS:
                mViewPager.setCurrentItem(1);
                break;
            case WEEKEND_STATUS:
                mViewPager.setCurrentItem(0);
                break;
            case LIST_OF_ALERTS:
                mViewPager.setCurrentItem(2);
                break;
            default:
                consumed = false;

        }

        return consumed;
    }
}
