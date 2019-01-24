package com.tfltravelalerts.ui.main.network_status_page

import android.view.View
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.common.StateMachine
import com.tfltravelalerts.store.network_status.NetworkStatusResponse
import com.tfltravelalerts.store.network_status.NetworkStatusStore
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.koin.core.parameter.parametersOf
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
        val interactions = get<NetworkStatusContract.Interactions> {
            parametersOf(subject, disposables, method)
        }
        val stateMachine = StateMachine(
                NetworkStatusContract.NetworkPageModel(null, false, null),
                get<NetworkStatusContract.Reducer>()
        )

        val disposable0 = stateMachine
                .observe()
                .map {
                    when (it.second) {
                        is NetworkStatusContract.Intent.Refresh ->
                            interactions.fetch()
                    }
                }
                .subscribe()
        disposables.add(disposable0)

        val disposable1 = view
                .getIntents()
                .mergeWith(subject)
                .observeOn(AndroidSchedulers.mainThread())
                // must subscribe in immediate thread to prevent missing first events
                .doOnNext { Logger.STATE_MACHINE.d("on event $it") }
                .map(stateMachine::onEvent)
                .doOnNext { Logger.STATE_MACHINE.d("new state $it") }
                .subscribe(view::render)
        disposables.add(disposable1)

        interactions.fetch() // get first data
    }
}
