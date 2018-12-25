package com.tfltravelalerts.ui.main.alarms_page

import android.annotation.SuppressLint
import android.content.Context
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.store.AlarmsStore
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailActivity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AlarmsPageInteractions(
        private val context: Context,
        private val alarmsStore: AlarmsStore
) : AlarmsPageContract.Interactions {
    override fun openAlarmDetail(alarm: ConfiguredAlarm) {
        AlarmDetailActivity.launchNewAlarm(context, alarm)
    }

    override fun createAlarm() {
        AlarmDetailActivity.launchNewAlarm(context)
    }

    @SuppressLint("CheckResult")
    override fun saveAlarm(alarm: ConfiguredAlarm) {
        // don't care about subscription - just try to do it
        Observable
                .just(0)
                // TODO really shouldn't have to do this in order to have a better UI
                .delay(350, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    alarmsStore.saveAlarm(alarm)
                }
    }
}
