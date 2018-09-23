package com.tfltravelalerts.ui.alarmdetail2

import com.tfltravelalerts.model.Time
import com.tfltravelalerts.store.AlarmsStore
import com.tfltravelalerts.ui.alarmdetail.UiData

class AlarmDetailPresenter : AlarmDetailContract.Presenter {
    // TODO make the following constructor parameters
    private lateinit var reducer: AlarmDetailStateReducer
    private lateinit var alarmsStore: AlarmsStore
    private lateinit var uiDataModelMapper: UiDataModelMapper

    // ---
    private lateinit var view: AlarmDetailContract.View

    override fun init(initialData: UiData, view: AlarmDetailContract.View) {
        this.view = view
        // TODO handle disposable
        val disposable = view.init(initialData)
                .subscribe {
                    view.render(reducer.onEvent(it))
                }
    }

    override fun save(): UiData {
        val value = uiDataModelMapper.map(reducer.lastState)
        return when (value) {
            is UiDataModelMapper.MapperResult.Success -> {
                alarmsStore.saveAlarm(value.configuredAlarm)
                reducer.lastState
            }
            is UiDataModelMapper.MapperResult.Fail ->
                // TODO map this appropriately in the right layer
                reducer.lastState.cloneWithErrorMessage("Failed to save")
        }

    }

    override fun openTimeSelection(): UiData {
        view.showTimePicker(reducer.lastState.time ?: Time(8, 0))
        return reducer.lastState
    }

    override fun onTimeSelected(time: Time) {
        view.render(reducer.onEvent(AlarmDetailContract.Intent.OnTimeSelected(time)))
    }
}
