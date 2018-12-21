package com.tfltravelalerts.ui.main.network_status_page

import com.tfltravelalerts.model.NetworkStatus
import io.reactivex.Observable

interface NetworkStatusContract {

    interface View {

        fun render(model: NetworkPageModel)

        fun getIntents(): Observable<Intent>
    }

    data class NetworkPageModel(
            val networkStatus: NetworkStatus,
            val loading: Boolean,
            val errorMessage: String?
    )

    sealed class Intent {
        object Refresh : Intent()
    }
}
