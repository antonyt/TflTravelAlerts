package com.tfltravelalerts.ui.main

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.tfltravelalerts.R
import com.tfltravelalerts.store.AlarmsStore
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AlarmsPageController(view: View, store: AlarmsStore) {
    init {
        val recyclerView = view.findViewById<RecyclerView>(R.id.main_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))
        val adapter = ConfiguredAlarmAdapter(view.context)
        recyclerView.adapter = adapter

        Observable
                .fromCallable { store.getAlarms().toMutableList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adapter.alarms = it }
    }
}