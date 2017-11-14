package com.tfltravelalerts.store

import android.support.annotation.WorkerThread
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.persistence.ConfiguredAlarmEntity

interface AlarmsStore {
    @WorkerThread
    fun getAlarms(): List<ConfiguredAlarm>

    @WorkerThread
    fun saveAlarm(alarm: ConfiguredAlarm)
}

class AlarmStoreDatabaseImpl(db: ConfiguredAlarmDatabase) : AlarmsStore {
    private val dao by lazy { db.alarmsDao() }

    override fun getAlarms(): List<ConfiguredAlarm> {
        return dao.getAlarms().map { it.alarm }
    }

    override fun saveAlarm(alarm: ConfiguredAlarm) {
        dao.insertOrUpdate(ConfiguredAlarmEntity(alarm))
    }
}