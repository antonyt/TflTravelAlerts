package com.tfltravelalerts.application

import android.app.Application
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.store.AlarmStoreDatabaseImpl
import com.tfltravelalerts.store.AlarmsStore

class TtaApplication : Application() {

    lateinit var alarmsStore: AlarmsStore

    override fun onCreate() {
        super.onCreate()
        val database = ConfiguredAlarmDatabase.getDatabase(this)
        alarmsStore = AlarmStoreDatabaseImpl(database)
    }
}