package com.tfltravelalerts.ui.main.network_status_page

import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.tfltravelalerts.R
import com.tfltravelalerts.ui.main.NetworkStatusAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class NetworkStatusView(view: View) : NetworkStatusContract.View {
    private val networkStatusAdapter = NetworkStatusAdapter()
    private val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.main_swipe_to_refresh)
    private val recyclerView: RecyclerView = view.findViewById(R.id.main_recycler_view)
    private val subject = PublishSubject.create<NetworkStatusContract.Intent>()
    private val snackbar: Snackbar

    init {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        recyclerView.adapter = networkStatusAdapter
        swipeRefreshLayout.setOnRefreshListener {
            subject.onNext(NetworkStatusContract.Intent.Refresh)
        }

        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
        // TODO handle dismissing (update the state)
    }

    override fun render(model: NetworkStatusContract.NetworkPageModel) {
        networkStatusAdapter.networkStatus = model.networkStatus
        swipeRefreshLayout.isRefreshing = model.loading
        if (model.errorMessage == null) {
            snackbar.dismiss()
        } else {
            snackbar.setText(model.errorMessage)
            snackbar.show()
        }
    }

    override fun getIntents(): Observable<NetworkStatusContract.Intent> {
        return subject
    }
}
