package com.tfltravelalerts.store

import android.support.annotation.WorkerThread
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.persistence.ConfiguredAlarmEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

interface AlarmsStore {
    @WorkerThread
    fun getAlarms(): List<ConfiguredAlarm>

    @WorkerThread
    fun saveAlarm(alarm: ConfiguredAlarm)

    fun observeAlarms(): Observable<List<ConfiguredAlarm>>
}

class AlarmStoreDatabaseImpl(db: ConfiguredAlarmDatabase) : AlarmsStore {
    private val dao by lazy { db.alarmsDao() }

    private val changes: BehaviorSubject<List<ConfiguredAlarm>> = BehaviorSubject.create()

    override fun getAlarms(): List<ConfiguredAlarm> {
        return dao.getAlarms().map { it.alarm }
    }

    override fun saveAlarm(alarm: ConfiguredAlarm) {
        dao.insertOrUpdate(ConfiguredAlarmEntity(alarm))
        changes.onNext(getAlarms())
    }

    override fun observeAlarms(): Observable<List<ConfiguredAlarm>> {
        if (!changes.hasValue()) {
            // TODO revisit this
            Observable
                    .fromCallable { getAlarms() }
                    .subscribeOn(Schedulers.io())
                    .subscribe { changes.onNext(it) }
        }
        return changes
    }
}
