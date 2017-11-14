package com.tfltravelalerts.ui.main

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
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
import com.tfltravelalerts.model.NetworkStatus
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.service.BackendService
import com.tfltravelalerts.store.AlarmStoreDatabaseImpl
import com.tfltravelalerts.store.NetworkStatusStore
import com.tfltravelalerts.store.NetworkStatusStoreImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
            recyclerView.adapter = adapter

            // TODO inject this
            val databaseImpl = AlarmStoreDatabaseImpl(ConfiguredAlarmDatabase.getDatabase(this@MainActivity))
            Observable
                    .fromCallable { databaseImpl.getAlarms().toMutableList() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    // TODO we could no longer be in resumed state
                    .subscribe { adapter.alarms = it }
        }

        private fun setupNetworkStatusView(view: View, position: Int) {
            NetworkStatusPageController(view, if (position == 0) networkStatusStore::getLiveNetworkStatus else networkStatusStore::getWeekendNetworkStatus)
        }
    }
}

class NetworkStatusPageController(view: View, val retriever: () -> NetworkStatus) {
    private val networkStatusAdapter = NetworkStatusAdapter()
    private val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.main_swipe_to_refresh)

    init {
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))

        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.main_swipe_to_refresh)
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        recyclerView.adapter = networkStatusAdapter
        refreshLayout.setOnRefreshListener {
            fetchNetworkStatusAsync()
        }
        fetchNetworkStatusAsync()
    }

    private fun fetchNetworkStatusAsync(): Disposable {
        return Observable
                .fromCallable { retriever.invoke() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { swipeRefreshLayout.isRefreshing = true }
                .doOnTerminate { swipeRefreshLayout.isRefreshing = false }
                .subscribe { networkStatusAdapter.networkStatus = it }
    }


}



