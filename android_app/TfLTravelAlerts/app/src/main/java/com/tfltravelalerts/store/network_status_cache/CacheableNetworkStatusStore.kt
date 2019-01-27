package com.tfltravelalerts.store.network_status_cache

import com.tfltravelalerts.model.NetworkStatus
import com.tfltravelalerts.store.network_status.NetworkStatusResponse
import io.reactivex.Maybe

interface CacheableNetworkStatusStore {

    fun saveLiveNetworkStatus(status: NetworkStatus)

    fun saveWeekendNetworkStatus(status: NetworkStatus)

    /*
     * Originally this interface extended NetworkStatusStore but the "getter" methods expose the
     * result as a Single, whereas the cache needs a Maybe. Alternatives:
     *
     * 1 - change NetworkStatusStore to return Observable which is compatible with both
     * 2 - this interface doesn't extend the other. Although that is the current implementation,
     *     it would be great if we could have them sharing the same [read] signature.
     */

    fun getLiveNetworkStatus(): Maybe<NetworkStatusResponse>

    fun getWeekendNetworkStatus(): Maybe<NetworkStatusResponse>
}
