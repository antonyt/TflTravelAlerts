package com.tfltravelalerts.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.tfltravelalerts.di.alarmDetailModule
import com.tfltravelalerts.di.alarmPageModule
import com.tfltravelalerts.di.globalModule
import com.tfltravelalerts.di.networkStatusPageModule
import com.tfltravelalerts.store.AlarmsStore
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

class TtaApplication : Application() {

    lateinit var alarmsStore: AlarmsStore

    override fun onCreate() {
        super.onCreate()

        startKoin(
                this,
                listOf(globalModule, alarmDetailModule, alarmPageModule, networkStatusPageModule),
                logger = AndroidLogger(true)
        )

        alarmsStore = getKoin().get()

        Stetho.initializeWithDefaults(this)
    }
}
