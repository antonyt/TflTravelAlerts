package com.tfltravelalerts.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tfltravelalerts.R
import com.tfltravelalerts.common.Assertions
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.common.ConstantViewPagerAdapter
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.service.BackendService
import com.tfltravelalerts.store.AlarmStoreDatabaseImpl
import com.tfltravelalerts.store.NetworkStatusStore
import com.tfltravelalerts.store.NetworkStatusStoreImpl
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity() {

    private val viewPagerAdapter by lazy { ConstantViewPagerAdapter(ViewPagerImpl()) }
    private val networkStatusStore: NetworkStatusStore by lazy { NetworkStatusStoreImpl(BackendService.createService()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setTitle(R.string.full_app_name)
        setSupportActionBar(main_toolbar)
        main_view_pager.adapter = viewPagerAdapter
        main_tab_strip.setupWithViewPager(main_view_pager)
        // TODO: finish the following lines:
//        addOnPageChangeListener(new TabChangeListener());
//        updateToolbarScrollingStatus();
    }

    inner class ViewPagerImpl : ConstantViewPagerAdapter.Implementation {
        override val pageCount = 3
        private val mViews: Array<View?> = arrayOfNulls<View?>(pageCount)
        private val mLayoutInflater: LayoutInflater by lazy { LayoutInflater.from(this@MainActivity) }

        override fun canPageScrollVertically(position: Int): Boolean {
            val view: View? = mViews[position]
            return view != null
                    && (view.canScrollVertically(1) || view.canScrollVertically(-1))
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> getString(R.string.main_tab_title_now)
                1 -> getString(R.string.main_tab_title_weekend)
                2 -> getString(R.string.main_tab_title_alarms)
                else -> {
                    Assertions.shouldNotHappen("invalid index: $position")
                    ""
                }
            }
        }

        override fun instantiateView(parent: ViewGroup, position: Int): View {
            if (position < 2) {
                val view = mLayoutInflater.inflate(R.layout.main_network_status, parent, false)
                setupNetworkStatusView(view, position)
                return view
            } else {
                val view = mLayoutInflater.inflate(R.layout.main_alarms_list, parent, false)
                setupAlarmsList(view)
                return view
            }
        }

        private fun setupAlarmsList(view: View) {
            // TODO inject this
            val database = ConfiguredAlarmDatabase.getDatabase(this@MainActivity)
            val impl = AlarmStoreDatabaseImpl(database)
            AlarmsPageController(view, impl)
        }

        private fun setupNetworkStatusView(view: View, position: Int) {
            NetworkStatusPageController(view, if (position == 0) networkStatusStore::getLiveNetworkStatus else networkStatusStore::getWeekendNetworkStatus)
        }
    }
}



