package com.tfltravelalerts.ui.alarmdetail

import com.tfltravelalerts.common.StateMachine
import com.tfltravelalerts.model.Time
import com.tfltravelalerts.store.AlarmsStore
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class AlarmDetailPresenter(
        private val alarmsStore: AlarmsStore,
        private val uiDataModelMapper: UiDataModelMapper

) : AlarmDetailContract.Presenter, KoinComponent {
    // needs to be lazy so we have the initial data
    private val machine by lazy { StateMachine(initialData, get<AlarmDetailContract.Reducer>()) }

    private lateinit var view: AlarmDetailContract.View
    private lateinit var initialData: UiData

    override fun init(initialData: UiData, view: AlarmDetailContract.View) {
        this.view = view
        this.initialData = initialData
        // TODO handle disposable
        val disposable = view.init(initialData)
                .subscribe {
                    view.render(machine.onEvent(it))
                }
    }

    override fun save(state: UiData): UiData {
        val value = uiDataModelMapper.map(state)
        return when (value) {
            is UiDataModelMapper.MapperResult.Success -> {
                Single
                        .fromCallable {
                            alarmsStore.saveAlarm(value.configuredAlarm)
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                view.finish()
                state
            }
            is UiDataModelMapper.MapperResult.Fail ->
                // TODO map this appropriately in the right layer
                state.cloneWithErrorMessage("Failed to save")
        }
    }

    override fun openTimeSelection(state: UiData): UiData {
        view.showTimePicker(state.time ?: Time(8, 0))
        return state
    }

    override fun onTimeSelected(time: Time) {
        view.render(machine.onEvent(AlarmDetailContract.Intent.OnTimeSelected(time)))
    }
}
