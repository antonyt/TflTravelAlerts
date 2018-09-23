package com.tfltravelalerts.ui.main.tabs

import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tfltravelalerts.R
import com.tfltravelalerts.application.TtaApplication
import com.tfltravelalerts.common.Assertions
import com.tfltravelalerts.common.ConstantViewPagerAdapter
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.service.BackendService
import com.tfltravelalerts.store.NetworkStatusStore
import com.tfltravelalerts.store.NetworkStatusStoreImpl
import com.tfltravelalerts.ui.main.AlarmsPageController
import com.tfltravelalerts.ui.main.NetworkStatusPageController

class TabsView(
        private val host: MainActivityTabsContract.Host)
    : MainActivityTabsContract.View {

    // TODO it would be cool to use some interface for view binding
    // so that we can write "val x: View by bind(R.id.abc)"
    // this class should implement the interface ViewBinder
    // and delegate it to the host
    //
    // e.g.: " ... : ViewBinder by host"

    private val toolbar: Toolbar by host.bind(R.id.main_toolbar)
    private val viewPager: ViewPager by host.bind(R.id.main_view_pager)
    private val appBarLayout: AppBarLayout by host.bind(R.id.main_app_bar_layout)
    private val tabLayout: TabLayout by host.bind(R.id.main_tab_strip)
    private val viewPagerAdapter by lazy { ConstantViewPagerAdapter(ViewPagerImpl()) }

    private val networkStatusStore: NetworkStatusStore by lazy { NetworkStatusStoreImpl(BackendService.createService()) }


    override fun render() {
        host.setTitle(R.string.full_app_name)
        host.setSupportActionBar(toolbar)

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(MyPageChangeListener())
        updateToolbarScrollingStatus()

    }


    private fun updateToolbarScrollingStatus() {
        if (viewPagerAdapter.canPageScrollVertically(viewPager.currentItem)) {
            turnOnToolbarScrolling()
        } else {
            turnOffToolbarScrolling()
        }
    }

    private fun turnOffToolbarScrolling() {
        Logger.d("MainActivity: turn off toolbar scrolling")
        val toolbarLayoutParams = toolbar.layoutParams as AppBarLayout.LayoutParams
        if (toolbarLayoutParams.scrollFlags != 0) {
            toolbarLayoutParams.scrollFlags = 0
            toolbar.layoutParams = toolbarLayoutParams
            val appBarLayoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
            appBarLayoutParams.behavior = null
            appBarLayout.layoutParams = appBarLayoutParams
        }
    }

    private fun turnOnToolbarScrolling() {
        Logger.d("MainActivity: turn on toolbar scrolling")
        val toolbarLayoutParams = toolbar.layoutParams as AppBarLayout.LayoutParams
        if (toolbarLayoutParams.scrollFlags == 0) {
            toolbarLayoutParams.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
            +AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            +AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            toolbar.layoutParams = toolbarLayoutParams
            val appBarLayoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
            appBarLayoutParams.behavior = AppBarLayout.Behavior()
            appBarLayout.layoutParams = appBarLayoutParams
        }
    }

    inner class ViewPagerImpl : ConstantViewPagerAdapter.Implementation {
        override val pageCount = 3
        private val scrollingViews = arrayOfNulls<View?>(pageCount)
        private val layoutInflater by lazy { LayoutInflater.from(viewPager.context) }

        override fun canPageScrollVertically(position: Int): Boolean {
            val view: View? = scrollingViews[position]
            return view != null
                    && (view.canScrollVertically(1) || view.canScrollVertically(-1))
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> host.getString(R.string.main_tab_title_now)
                1 -> host.getString(R.string.main_tab_title_weekend)
                2 -> host.getString(R.string.main_tab_title_alarms)
                else -> {
                    Assertions.shouldNotHappen("invalid index: $position")
                    ""
                }
            }
        }

        override fun instantiateView(parent: ViewGroup, position: Int): View {
            return if (position < 2) {
                val view = layoutInflater.inflate(R.layout.main_network_status, parent, false)
                scrollingViews[position] = setupNetworkStatusView(view, position)
                view
            } else {
                val view = layoutInflater.inflate(R.layout.main_alarms_list, parent, false)
                scrollingViews[position] = setupAlarmsList(view)
                view
            }
        }

        private fun setupAlarmsList(view: View): View {
            val controller = AlarmsPageController(view, (view.context.applicationContext as TtaApplication).alarmsStore)
            return controller.recyclerView
        }

        private fun setupNetworkStatusView(view: View, position: Int): View {
            val controller = NetworkStatusPageController(view, if (position == 0) networkStatusStore::getLiveNetworkStatus else networkStatusStore::getWeekendNetworkStatus)
            return controller.recyclerView
        }
    }

    inner class MyPageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                updateToolbarScrollingStatus()
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {}
    }
}
