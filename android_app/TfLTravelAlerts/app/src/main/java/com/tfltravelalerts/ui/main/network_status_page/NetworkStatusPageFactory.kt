package com.tfltravelalerts.ui.main.network_status_page

import android.view.View
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.store.NetworkStatusResponse
import com.tfltravelalerts.store.NetworkStatusStore
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class NetworkStatusPageFactory : KoinComponent {

    fun createLiveView(root: View, disposables: CompositeDisposable) {
        return createView(root, disposables, NetworkStatusStore::getLiveNetworkStatus)
    }

    fun createWeekendView(root: View, disposables: CompositeDisposable) {
        return createView(root, disposables, NetworkStatusStore::getWeekendNetworkStatus)
    }

    private fun createView(
            root: View,
            disposables: CompositeDisposable,
            method: NetworkStatusStore.() -> Single<NetworkStatusResponse>
    ) {
        val view = NetworkStatusView(root)
        val subject = PublishSubject.create<NetworkStatusContract.Intent>()
        val interactions = object : NetworkStatusContract.Interactions {
            override fun fetch() {
                subject.onNext(NetworkStatusContract.Intent.FetchingData)
                val store = get<NetworkStatusStore>()
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

        val stateMachine = NetworkStatusStateMachineImpl(
                interactions,
                NetworkStatusContract.NetworkPageModel(null, false, null)
        )

        val disposable = view
                .getIntents()
                .mergeWith(subject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext { Logger.d("on event $it") }
                .map(stateMachine::onEvent)
                .doOnNext { Logger.d("new state $it") }
                .subscribe(view::render)
        disposables.add(disposable)

        interactions.fetch() // get first data
    }
}
