package com.tfltravelalerts.ui.alarmdetail2

import com.tfltravelalerts.model.Time
import com.tfltravelalerts.store.AlarmsStore
import com.tfltravelalerts.ui.alarmdetail.UiData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class AlarmDetailPresenter(
        private val alarmsStore: AlarmsStore,
        private val uiDataModelMapper: UiDataModelMapper

) : AlarmDetailContract.Presenter, KoinComponent {
    private val machine: AlarmDetailStateMachine by inject { parametersOf(initialData) }


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

    override fun save(): UiData {
        val value = uiDataModelMapper.map(machine.lastState)
        return when (value) {
            is UiDataModelMapper.MapperResult.Success -> {
                Single
                        .fromCallable {
                            alarmsStore.saveAlarm(value.configuredAlarm)
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                view.finish()
                machine.lastState
            }
            is UiDataModelMapper.MapperResult.Fail ->
                // TODO map this appropriately in the right layer
                machine.lastState.cloneWithErrorMessage("Failed to save")
        }

    }

    override fun openTimeSelection(): UiData {
        view.showTimePicker(machine.lastState.time ?: Time(8, 0))
        return machine.lastState
    }

    override fun onTimeSelected(time: Time) {
        view.render(machine.onEvent(AlarmDetailContract.Intent.OnTimeSelected(time)))
    }
}
