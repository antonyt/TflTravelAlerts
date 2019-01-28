package com.tfltravelalerts.orchestrator.network_status

import com.tfltravelalerts.store.network_status.NetworkStatusResponse
import com.tfltravelalerts.store.network_status.NetworkStatusStore
import com.tfltravelalerts.store.network_status_cache.CacheableNetworkStatusStore
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class NetworkStatusOrchestratorImpl(
        private val cacheStore: CacheableNetworkStatusStore,
        private val networkStore: NetworkStatusStore
) : NetworkStatusOrchestrator {
    private val liveChanges: BehaviorSubject<NetworkStatusResponse> = BehaviorSubject.create()
    private val weekendChanges: BehaviorSubject<NetworkStatusResponse> = BehaviorSubject.create()

    override fun observeLiveNetworkStatus(): Observable<NetworkStatusResponse> = liveChanges

    override fun observeWeekendNetworkStatus(): Observable<NetworkStatusResponse> = weekendChanges

    override fun fetchLiveNetworkStatus() {
        // TODO make sure we don't need to worry about the disposable
        fetchData(cacheStore::getLiveNetworkStatus, networkStore::getLiveNetworkStatus)
                .subscribe(liveChanges)
    }

    override fun fetchWeekendNetworkStatus() {
        fetchData(cacheStore::getWeekendNetworkStatus, networkStore::getWeekendNetworkStatus)
                .subscribe(weekendChanges)
    }

    private fun fetchData(
            getCached: () -> Maybe<NetworkStatusResponse>,
            getNetwork: () -> Single<NetworkStatusResponse>
    ): Observable<NetworkStatusResponse> {
        // TODO verify what happens with errors
        // TODO add unit tests
        val subject: BehaviorSubject<Observable<NetworkStatusResponse>> = BehaviorSubject.create()

        val cachedStatus = getCached()
        subject.onNext(cachedStatus.toObservable())

        val freshStatus = getNetwork()
        val freshStatusDisposable = freshStatus.subscribe { it: NetworkStatusResponse ->
            subject.onNext(Observable.just(it))
        }

        return Observable.switchOnNext(subject).doOnDispose {
            freshStatusDisposable.dispose()
        }
    }

    private fun fetchData2(
            getCached: () -> Maybe<NetworkStatusResponse>,
            getNetwork: () -> Single<NetworkStatusResponse>
    ): Observable<NetworkStatusResponse> {

        return getCached().toObservable().mergeWith(getNetwork().toObservable()).scan { t1: NetworkStatusResponse, t2: NetworkStatusResponse ->
            when {
                t1 is NetworkStatusResponse.Success && t2 is NetworkStatusResponse.Success -> {
                    if (t1.networkStatus.date > t1.networkStatus.date) {
                        t1
                    } else {
                        t2
                    }
                }
                t1 is NetworkStatusResponse.Success -> {
                    t1
                }
                t2 is NetworkStatusResponse.Success -> {
                    t2
                }
                t1 is NetworkStatusResponse.NetworkError
                        || t2 is NetworkStatusResponse.NetworkError -> {
                    NetworkStatusResponse.NetworkError
                }
                t1 is NetworkStatusResponse.UnknownError -> {
                    t1
                }
                t2 is NetworkStatusResponse.UnknownError -> {
                    t2
                    // not finishing this; just leaving it here for the record
                }
                else -> {
                    t1
                }
            }
        }
    }
}
