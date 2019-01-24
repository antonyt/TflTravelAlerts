package com.tfltravelalerts.store

import com.tfltravelalerts.model.NetworkStatus

sealed class NetworkStatusResponse {
    data class Success(val networkStatus: NetworkStatus) : NetworkStatusResponse()

    object NetworkError : NetworkStatusResponse()

    data class UnknownError(val throwable: Throwable? = null) : NetworkStatusResponse()
}
