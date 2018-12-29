package com.tfltravelalerts.ui.main.alarms_page

import android.view.View
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.common.StateMachine
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

        val interactions = get<AlarmsPageContract.Interactions> { parametersOf(root.context) }
        val stateMachine = StateMachine(listOf(), get<AlarmsPageContract.Reducer>())

        val disposable0 = stateMachine
                .observe()
                .map { (_, event) ->
                    when (event) {
                        is AlarmsPageContract.Intent.OpenAlarm -> {
                            interactions.openAlarmDetail(event.alarm)
                        }
                        is AlarmsPageContract.Intent.ToggleAlarm -> {
                            val updatedAlarm = event.alarm.copy(enabled = event.isEnabled)
                            // TODO what if we needed to emit another event after this save alarm?
                            // if saveAlarm had a "in progress + done / success" events we could deal with that
                            // better
                            interactions.saveAlarm(updatedAlarm)
                        }
                        AlarmsPageContract.Intent.CreateAlarm -> {
                            interactions.createAlarm()
                        }
                    }
                }
                .subscribe()
        disposables.add(disposable0)

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
