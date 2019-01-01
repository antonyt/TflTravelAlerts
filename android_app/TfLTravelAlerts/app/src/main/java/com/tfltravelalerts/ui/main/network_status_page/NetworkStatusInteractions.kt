package com.tfltravelalerts.ui.main.network_status_page

import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.store.NetworkStatusResponse
import com.tfltravelalerts.store.NetworkStatusStore
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NetworkStatusInteractions(
        private val subject: Observer<NetworkStatusContract.Intent>,
        private val store: NetworkStatusStore,
        private val disposables: CompositeDisposable,
        private val method: NetworkStatusStore.() -> Single<NetworkStatusResponse>
) : NetworkStatusContract.Interactions {
    override fun fetch() {
        subject.onNext(NetworkStatusContract.Intent.FetchingData)
        disposables.add(
                method.invoke(store)
                        .subscribeOn(Schedulers.io())
                        .subscribe { response ->
                            when (response) {
                                is NetworkStatusResponse.Success ->
                                    subject.onNext(NetworkStatusContract.Intent.ResultReceived(response.networkStatus))
                                NetworkStatusResponse.NetworkError ->
                                    subject.onNext(NetworkStatusContract.Intent.ErrorReceived("No internet. Please try again"))
                                is NetworkStatusResponse.UnknownError -> {
                                    Logger.d("Unknown error", response.throwable)
                                    subject.onNext(NetworkStatusContract.Intent.ErrorReceived("Some error occurred. Please try again"))
                                }
                            }
                        }
        )
    }
}
