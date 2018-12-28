package com.tfltravelalerts.di

import android.arch.persistence.room.Room
import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tfltravelalerts.BuildConfig
import com.tfltravelalerts.di.Scopes.ALARM_DETAIL_SCREEN
import com.tfltravelalerts.di.Scopes.MAIN_SCREEN
import com.tfltravelalerts.json.LineJsonTypeAdapter
import com.tfltravelalerts.json.StatusJsonTypeAdapter
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.Status
import com.tfltravelalerts.persistence.ConfiguredAlarmDatabase
import com.tfltravelalerts.service.BackendService
import com.tfltravelalerts.store.AlarmStoreDatabaseImpl
import com.tfltravelalerts.store.AlarmsStore
import com.tfltravelalerts.store.NetworkStatusStore
import com.tfltravelalerts.store.NetworkStatusStoreImpl
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailContract
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailPresenter
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailStateMachine
import com.tfltravelalerts.ui.alarmdetail.AlarmDetailStateReducerImpl
import com.tfltravelalerts.ui.alarmdetail.UiData
import com.tfltravelalerts.ui.alarmdetail.UiDataModelMapper
import com.tfltravelalerts.ui.main.alarms_page.AlarmsPageContract
import com.tfltravelalerts.ui.main.alarms_page.AlarmsPageInteractions
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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

val networkStatusPageModule = module {
    single<NetworkStatusStore> {
        NetworkStatusStoreImpl(get())
    }

    single<BackendService> {
        Retrofit.Builder()
                .baseUrl(BackendService.BASE_URL)
                .client(get())
                .addConverterFactory(get())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(BackendService::class.java)
    }

    single<Gson> {
        GsonBuilder()
                .registerTypeAdapter(Line::class.java, LineJsonTypeAdapter())
                .registerTypeAdapter(Status::class.java, StatusJsonTypeAdapter())
                .create()
    }

    single<Converter.Factory> {
        GsonConverterFactory.create(get())
    }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
    }
}

