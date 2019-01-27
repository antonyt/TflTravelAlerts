package com.tfltravelalerts.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.tfltravelalerts.di.alarmDetailModule
import com.tfltravelalerts.di.alarmPageModule
import com.tfltravelalerts.di.cacheableNetworkStatusStoreModule
import com.tfltravelalerts.di.globalModule
import com.tfltravelalerts.di.networkStatusPageModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

class TtaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
                this,
                listOf(
                        globalModule,
                        alarmDetailModule,
                        alarmPageModule,
                        networkStatusPageModule,
                        cacheableNetworkStatusStoreModule
                ),
                logger = AndroidLogger(true)
        )

        Stetho.initializeWithDefaults(this)
    }
}
