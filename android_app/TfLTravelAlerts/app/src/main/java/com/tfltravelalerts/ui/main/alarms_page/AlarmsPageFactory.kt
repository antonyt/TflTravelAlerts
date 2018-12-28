package com.tfltravelalerts.ui.main.alarms_page

import android.view.View
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.store.AlarmsStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class AlarmsPageFactory : KoinComponent {

    fun createView(root: View, disposables: CompositeDisposable) {
        val view = AlarmsPageView(root)

        val interactions = getKoin().get<AlarmsPageContract.Interactions> { parametersOf(root.context) }
        val stateMachine = AlarmsPageStateMachine(listOf(), interactions)

        val store = get<AlarmsStore>()
        val disposable = store
                .observeAlarms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map<AlarmsPageContract.Intent> { AlarmsPageContract.Intent.AlarmsUpdated(it) }
                .mergeWith(view.getIntents())
                .doOnNext { Logger.d("on event $it") }
                .map(stateMachine::onEvent)
                .doOnNext { Logger.d("new state $it") }
                .subscribe(view::render)

        disposables.add(disposable)
    }
}