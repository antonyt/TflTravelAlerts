package com.tfltravelalerts.store

import android.support.annotation.WorkerThread
import io.reactivex.Single

interface NetworkStatusStore {
    @WorkerThread
    fun getLiveNetworkStatus(): Single<NetworkStatusResponse>

    @WorkerThread
    fun getWeekendNetworkStatus(): Single<NetworkStatusResponse>
}
