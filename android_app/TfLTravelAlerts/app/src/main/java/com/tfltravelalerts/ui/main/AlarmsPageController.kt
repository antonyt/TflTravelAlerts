package com.tfltravelalerts.ui.main

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.tfltravelalerts.R
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.store.AlarmsStore
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

// TODO delete me
class AlarmsPageController(view: View, private val store: AlarmsStore) : ConfiguredAlarmAdapter.ViewActions {
    val recyclerView: RecyclerView = view.findViewById(R.id.main_recycler_view)
    val context: Context = view.context
    private var disposable: Disposable?

    init {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))
        val adapter = ConfiguredAlarmAdapter(this, view.context)
        recyclerView.adapter = adapter

        disposable = store.observeAlarms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { adapter.alarms = it; Log.d("TTL", "on result ${it.size} alarms") },
                        { Log.d("TTL", "err", it) },
                        { Log.d("TTL", "completed") }
                )

    }

    override fun onAlarmClicked(alarm: ConfiguredAlarm) {
        AlarmDetailActivity.launchNewAlarm(context, alarm)
    }

    override fun onEnabledChanged(alarm: ConfiguredAlarm, isEnabled: Boolean) {
        Single
                .fromCallable { store.saveAlarm(alarm.withEnabledValue(isEnabled)) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun onAddAlarmClicked() {
        AlarmDetailActivity.launchNewAlarm(context)
    }
}
