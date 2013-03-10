
package com.tfltravelalerts;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.tfltravelalerts.alerts.ViewAlertsFragment;
import com.tfltravelalerts.statusviewer.LineStatusViewerListFragment;
import com.viewpagerindicator.PageIndicator;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        
        // TODO: fixup
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

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
        indicator.setViewPager(viewPager);
    }
}
