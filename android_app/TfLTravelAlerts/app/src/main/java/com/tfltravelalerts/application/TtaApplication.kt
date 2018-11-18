package com.tfltravelalerts.application

import android.app.Application
import com.tfltravelalerts.di.alarmDetailModule
import com.tfltravelalerts.di.globalModule
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.store.AlarmStoreDatabaseImpl
import com.tfltravelalerts.store.AlarmsStore
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

class TtaApplication : Application() {

    lateinit var alarmsStore: AlarmsStore

    override fun onCreate() {
        super.onCreate()
        val database = ConfiguredAlarmDatabase.getDatabase(this)
        alarmsStore = AlarmStoreDatabaseImpl(database)

        startKoin(
                this,
                listOf(globalModule, alarmDetailModule),
                logger = AndroidLogger(true)
        )

    }
}
