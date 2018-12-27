package com.tfltravelalerts.ui.main

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tfltravelalerts.R
import com.tfltravelalerts.common.Assertions
import com.tfltravelalerts.common.BaseActivity
import com.tfltravelalerts.common.ConstantViewPagerAdapter
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.di.Scopes
import com.tfltravelalerts.ui.main.alarms_page.AlarmsPageFactory
import com.tfltravelalerts.ui.main.network_status_page.NetworkStatusPageFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_alarms_list.view.*
import kotlinx.android.synthetic.main.main_network_status.view.*
import org.koin.android.ext.android.getKoin


class MainActivity : BaseActivity() {

    private val disposables = CompositeDisposable()
    private val viewPagerAdapter by lazy { ConstantViewPagerAdapter(ViewPagerImpl()) }

    // these are some sort of "alias" to keep the naming conventions
    private val toolbar by lazy { main_toolbar }
    private val appBarLayout by lazy { main_app_bar_layout }
    private val viewPager by lazy { main_view_pager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        getKoin().createScope(Scopes.MAIN_SCREEN)
        setTitle(R.string.full_app_name)
        setSupportActionBar(main_toolbar)
        viewPager.adapter = viewPagerAdapter
        main_tab_strip.setupWithViewPager(viewPager)

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
        private val scrollingViews: Array<View?> = arrayOfNulls<View?>(pageCount)
        private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(this@MainActivity) }

        override fun canPageScrollVertically(position: Int): Boolean {
            val view: View? = scrollingViews[position]
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

        private fun setupAlarmsList(root: View): View {
            AlarmsPageFactory().createView(root, disposables)
            return root.alarms_recycler_view
        }

        private fun setupNetworkStatusView(root: View, position: Int): View {
            val factory = NetworkStatusPageFactory()
            val method =
                    if (position == 0) {
                        factory::createLiveView
                    } else {
                        factory::createWeekendView
                    }
            method.invoke(root, disposables)
            return root.network_status_recycler_view
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        getKoin().getScope(Scopes.MAIN_SCREEN).close()
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



