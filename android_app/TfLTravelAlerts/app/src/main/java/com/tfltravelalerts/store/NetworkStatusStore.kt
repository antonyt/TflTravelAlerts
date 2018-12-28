package com.tfltravelalerts.store

import android.support.annotation.WorkerThread
import com.tfltravelalerts.model.LineStatus
import com.tfltravelalerts.model.NetworkStatus
import com.tfltravelalerts.service.BackendService
import io.reactivex.Single
import java.io.IOException
import java.util.*

interface NetworkStatusStore {
    @WorkerThread
    fun getLiveNetworkStatus(): Single<NetworkStatusResponse>

    @WorkerThread
    fun getWeekendNetworkStatus(): Single<NetworkStatusResponse>
}

sealed class NetworkStatusResponse {
    data class Success(val networkStatus: NetworkStatus) : NetworkStatusResponse()

    object NetworkError : NetworkStatusResponse()

    data class UnknownError(val throwable: Throwable? = null) : NetworkStatusResponse()
}

// TODO add cached network status store

class NetworkStatusStoreImpl(private val backend: BackendService) : NetworkStatusStore {
    override fun getLiveNetworkStatus(): Single<NetworkStatusResponse> {
        return fetchStatus(backend.getLiveNetworkStatus())
    }

    override fun getWeekendNetworkStatus(): Single<NetworkStatusResponse> {
        return fetchStatus(backend.getWeekendNetworkStatus())
    }

    private fun fetchStatus(stream: Single<List<LineStatus>>): Single<NetworkStatusResponse> {
        return stream
                .map {
                    it.filter {
                        // coming from json it can actually be null!
                        @Suppress("SENSELESS_COMPARISON")
                        it.line != null
                    }
                }
                .map {
                    NetworkStatus(Date(), it)
                }
                .map<NetworkStatusResponse> { NetworkStatusResponse.Success(it) }
                .onErrorReturn {
                    if (it is IOException) {
                        NetworkStatusResponse.NetworkError
                    } else {
                        NetworkStatusResponse.UnknownError(it)
                    }
                }
    }
}
