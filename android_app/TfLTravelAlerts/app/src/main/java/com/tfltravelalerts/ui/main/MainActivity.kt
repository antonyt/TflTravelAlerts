package com.tfltravelalerts.ui.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tfltravelalerts.R
import com.tfltravelalerts.common.Assertions
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.common.ConstantViewPagerAdapter
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.NetworkStatus
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseActivity() {

    private val mViewPagerAdapter by lazy { ConstantViewPagerAdapter(ViewPagerImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setTitle(R.string.full_app_name)
        setSupportActionBar(main_toolbar)
        main_view_pager.adapter = mViewPagerAdapter
        main_tab_strip.setupWithViewPager(main_view_pager)
        // TODO: finish the following lines:
//        addOnPageChangeListener(new TabChangeListener());
//        updateToolbarScrollingStatus();
//        getInitialData();
    }

    inner class ViewPagerImpl : ConstantViewPagerAdapter.Implementation {
        override val pageCount = 3
        val mViews: Array<View?> = arrayOfNulls<View?>(pageCount)
        val mLayoutInflater: LayoutInflater by lazy  { LayoutInflater.from(this@MainActivity)}

        override fun canPageScrollVertically(position: Int): Boolean {
            val view: View? = mViews[position]
            return view != null
                    &&
                    (view.canScrollVertically(1) || view.canScrollVertically(-1))
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return getString(R.string.main_tab_title_now)
                1 -> return getString(R.string.main_tab_title_weekend)
                2 -> return getString(R.string.main_tab_title_alarms)
                else -> {
                    Assertions.shouldNotHappen("invalid index: $position")
                    return ""
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
            val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))
            val adapter = ConfiguredAlarmAdapter(this@MainActivity)
            adapter.alarms = ConfiguredAlarm.alarms
            recyclerView.adapter = adapter
        }

        private fun setupNetworkStatusView(view: View, position: Int) {
            val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))

            val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.main_swipe_to_refresh)
            refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
            // TODO
            // setup swipe refresh listener
            // set in progress if download is in progress
            val networkStatusAdapter = NetworkStatusAdapter()
            recyclerView.adapter = networkStatusAdapter
            networkStatusAdapter.networkStatus = if(position == 0)  NetworkStatus.LIVE_STATUS else NetworkStatus.WEEKEND_STATUS

        }
    }
}




