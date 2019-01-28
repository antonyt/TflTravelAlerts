package com.tfltravelalerts.orchestrator.network_status

import com.tfltravelalerts.store.network_status.NetworkStatusResponse
import io.reactivex.Observable

interface NetworkStatusOrchestrator {
    // TODO should we get a continuous stream or just once with at most 2 results (cache + network)?
    fun observeLiveNetworkStatus(): Observable<NetworkStatusResponse>

    fun observeWeekendNetworkStatus(): Observable<NetworkStatusResponse>

    fun fetchLiveNetworkStatus()

    fun fetchWeekendNetworkStatus()
}
