package com.tfltravelalerts.ui.alarmdetail

import com.tfltravelalerts.model.Time
import com.tfltravelalerts.store.AlarmsStore
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AlarmDetailInteractions(
        private val uiDataModelMapper: UiDataModelMapper,
        private val alarmsStore: AlarmsStore,
        private val observer: Observer<AlarmDetailContract.Intent>,
        private val view: AlarmDetailContract.View
) : AlarmDetailContract.Interactions {
    override fun save(state: UiData) {
        val value = uiDataModelMapper.map(state)
        when (value) {
            is UiDataModelMapper.MapperResult.Success -> {
                Single
                        .fromCallable {
                            alarmsStore.saveAlarm(value.configuredAlarm)
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                observer.onNext(AlarmDetailContract.Intent.CloseView)
            }
            is UiDataModelMapper.MapperResult.Fail ->
                // TODO map this appropriately in the right layer
                observer.onNext(AlarmDetailContract.Intent.ErrorUpdated("Please select the time for this alarm"))
        }
    }

    override fun openTimeSelection(state: UiData) {
        view.showTimePicker(state.time ?: Time(8, 0))
    }

    override fun closeView() {
        view.finish()
    }
}
