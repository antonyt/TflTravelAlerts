package com.tfltravelalerts.store

import android.support.annotation.WorkerThread
import com.tfltravelalerts.model.LineStatus
import com.tfltravelalerts.model.NetworkStatus
import com.tfltravelalerts.service.BackendService
import retrofit2.Call
import java.io.IOException
import java.util.*

interface NetworkStatusStore {
    @WorkerThread
    fun getLiveNetworkStatus(): NetworkStatusResponse

    @WorkerThread
    fun getWeekendNetworkStatus(): NetworkStatusResponse
}

sealed class NetworkStatusResponse {
    data class Success(val networkStatus: NetworkStatus) : NetworkStatusResponse()

    object NetworkError : NetworkStatusResponse()

    data class UnknownError(val throwable: Throwable? = null) : NetworkStatusResponse()
}

class NetworkStatusStoreImpl(private val backend: BackendService) : NetworkStatusStore {
    override fun getLiveNetworkStatus(): NetworkStatusResponse {
        return fetchStatus(backend.getLiveNetworkStatus())
    }

    override fun getWeekendNetworkStatus(): NetworkStatusResponse {
        return fetchStatus(backend.getWeekendNetworkStatus())

    }

    private fun fetchStatus(call: Call<List<LineStatus>>): NetworkStatusResponse {
        try {
            val response = call.execute()
            return if (response.isSuccessful) {
                val status = response
                        .body()!!
                        .filter {
                            // coming from json it can actually be null!
                            @Suppress("SENSELESS_COMPARISON")
                            it.line != null
                        }
                NetworkStatusResponse.Success(NetworkStatus(Date(), status))
            } else {
                NetworkStatusResponse.UnknownError()
            }
        } catch (e: Exception) {
            return if (e is IOException) {
                NetworkStatusResponse.NetworkError
            } else {
                NetworkStatusResponse.UnknownError(e)
            }
        }
    }
}

// TODO add cached network status store
