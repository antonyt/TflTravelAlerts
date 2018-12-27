package com.tfltravelalerts.ui.main.network_status_page

class NetworkStatusStateMachineImpl(
        private val interactions: NetworkStatusContract.Interactions,
        initialState: NetworkStatusContract.NetworkPageModel
) : NetworkStatusContract.StateMachine(initialState) {
    override fun reduceState(currentState: NetworkStatusContract.NetworkPageModel, event: NetworkStatusContract.Intent): NetworkStatusContract.NetworkPageModel {
        return when (event) {
            NetworkStatusContract.Intent.Refresh -> {
                interactions.fetch()
                currentState
            }
            NetworkStatusContract.Intent.FetchingData -> currentState.copy(loading = true)
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
