package com.tfltravelalerts.store

import android.support.annotation.WorkerThread
import com.tfltravelalerts.model.NetworkStatus
import com.tfltravelalerts.service.BackendService
import java.util.*

interface NetworkStatusStore {
    @WorkerThread
    fun getLiveNetworkStatus(): NetworkStatus

    @WorkerThread
    fun getWeekendNetworkStatus(): NetworkStatus
}


class NetworkStatusStoreImpl(private val backend: BackendService) : NetworkStatusStore {
    override fun getLiveNetworkStatus(): NetworkStatus {
        val status = backend.getLiveNetworkStatus()
                .execute()
                .body()!!
                .filter {
                    // coming from json it can actually be null!
                    @Suppress("SENSELESS_COMPARISON")
                    it.line != null
                }
        return NetworkStatus(Date(), status)
    }

    override fun getWeekendNetworkStatus(): NetworkStatus {
        val status = backend.getWeekendNetworkStatus()
                .execute()
                .body()!!
                .filter {
                    // coming from json it can actually be null!
                    @Suppress("SENSELESS_COMPARISON")
                    it.line != null
                }
        return NetworkStatus(Date(), status)
    }
}

// TODO add cached network status store