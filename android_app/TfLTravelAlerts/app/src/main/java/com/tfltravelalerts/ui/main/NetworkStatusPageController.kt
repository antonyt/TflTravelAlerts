package com.tfltravelalerts.ui.main

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.tfltravelalerts.R
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.model.NetworkStatus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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
                .doOnNext { Logger.d("network status: $it") }
                .subscribe { networkStatusAdapter.networkStatus = it }
    }


}