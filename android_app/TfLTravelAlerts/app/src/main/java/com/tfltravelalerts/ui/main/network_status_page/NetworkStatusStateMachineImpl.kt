package com.tfltravelalerts.ui.main.network_status_page

class NetworkStatusReducer : NetworkStatusContract.Reducer {
    override fun reduce(currentState: NetworkStatusContract.NetworkPageModel, event: NetworkStatusContract.Intent): NetworkStatusContract.NetworkPageModel {
        return when (event) {
            NetworkStatusContract.Intent.Refresh ->
                currentState
            NetworkStatusContract.Intent.FetchingData ->
                currentState.copy(loading = true)
            is NetworkStatusContract.Intent.ResultReceived ->
                currentState.copy(
                        loading = false,
                        errorMessage = null,
                        networkStatus = event.networkStatus
                )
            is NetworkStatusContract.Intent.ErrorReceived ->
                currentState.copy(
                        errorMessage = event.message,
                        loading = false
                )
        }
    }
}
