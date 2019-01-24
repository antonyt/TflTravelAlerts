package com.tfltravelalerts.store.configured_alarm

import android.support.annotation.WorkerThread
import com.tfltravelalerts.model.ConfiguredAlarm
import io.reactivex.Observable

interface AlarmsStore {
    @WorkerThread
    fun getAlarms(): List<ConfiguredAlarm>

    @WorkerThread
    fun saveAlarm(alarm: ConfiguredAlarm)

    fun observeAlarms(): Observable<List<ConfiguredAlarm>>
}

