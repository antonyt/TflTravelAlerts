package com.tfltravelalerts.store

import com.tfltravelalerts.model.NetworkStatus

interface CacheableNetworkStatusStore {

    fun saveLiveNetworkStatus(status: NetworkStatus)

    fun saveWeekendNetworkStatus(status: NetworkStatus)
}
