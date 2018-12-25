package com.tfltravelalerts.di

import android.arch.persistence.room.Room
import android.content.Context
import com.tfltravelalerts.di.Scopes.ALARM_DETAIL_SCREEN
import com.tfltravelalerts.di.Scopes.MAIN_SCREEN
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.store.AlarmStoreDatabaseImpl
import com.tfltravelalerts.store.AlarmsStore
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailContract
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailPresenter
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailStateMachine
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailStateReducerImpl
import com.tfltravelalerts.ui.alarmdetail.UiData
import com.tfltravelalerts.ui.alarmdetail.UiDataModelMapper
import com.tfltravelalerts.ui.main.alarms_page.AlarmsPageContract
import com.tfltravelalerts.ui.main.alarms_page.AlarmsPageInteractions
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

object Scopes {
    const val ALARM_DETAIL_SCREEN = "AlarmDetailScreenScope"
    const val MAIN_SCREEN = "MainScreenScope"
}

val globalModule = module {

    single {
        Room.databaseBuilder(
                androidContext(),
                ConfiguredAlarmDatabase::class.java,
                "alarm_database.db"
        ).build()
    }


    single<AlarmsStore> { AlarmStoreDatabaseImpl(get()) }
}

val alarmDetailModule = module {

    single {
        UiDataModelMapper()
    }

    scope<AlarmDetailContract.Presenter>(ALARM_DETAIL_SCREEN) {
        AlarmDetailPresenter(get(), get())
    } bind AlarmDetailContract.UiInteractions::class

    factory<AlarmDetailStateMachine> { (initialState: UiData) ->
        AlarmDetailStateReducerImpl(initialState, get())
    }
}

val alarmPageModule = module {
    scope<AlarmsPageContract.Interactions>(MAIN_SCREEN) { (context: Context) ->
        AlarmsPageInteractions(context, get())
    }
}

