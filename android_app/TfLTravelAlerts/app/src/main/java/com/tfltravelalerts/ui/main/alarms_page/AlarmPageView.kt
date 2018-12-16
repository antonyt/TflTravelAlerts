package com.tfltravelalerts.ui.main.alarms_page

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.tfltravelalerts.R
import com.tfltravelalerts.model.ConfiguredAlarm
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AlarmPageView(view: View) : AlarmsPageContract.View {
    private val recyclerView: RecyclerView = view.findViewById(R.id.main_recycler_view)
    private val adapter: ConfiguredAlarmAdapter
    private val subject = PublishSubject.create<AlarmsPageContract.Intent>()

    init {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, LinearLayout.VERTICAL))
        adapter = ConfiguredAlarmAdapter(subject, view.context)
        recyclerView.adapter = adapter
    }

    override fun render(alarms: List<ConfiguredAlarm>) {
        adapter.alarms = alarms
    }

    override fun getIntents(): Observable<AlarmsPageContract.Intent> = subject
}
