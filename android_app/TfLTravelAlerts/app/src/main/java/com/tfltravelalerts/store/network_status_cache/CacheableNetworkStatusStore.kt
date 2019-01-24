package com.tfltravelalerts.store.network_status_cache

import com.tfltravelalerts.model.NetworkStatus

interface CacheableNetworkStatusStore {

    fun saveLiveNetworkStatus(status: NetworkStatus)

    fun saveWeekendNetworkStatus(status: NetworkStatus)
}
