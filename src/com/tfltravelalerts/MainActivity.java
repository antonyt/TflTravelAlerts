
package com.tfltravelalerts;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;

import com.tfltravelalerts.alerts.LineStatusAlertsFragment;
import com.tfltravelalerts.statusviewer.LineStatusViewerListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: fix up
        ViewPager viewPager = new ViewPager(this);
        viewPager.setId(1);
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
                        return new LineStatusAlertsFragment();
                    default:
                        return null;
                }

            }
        });
        
        setContentView(viewPager);

    }
}
