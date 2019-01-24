package com.tfltravelalerts.ui.alarmdetail

import com.tfltravelalerts.model.Time
import com.tfltravelalerts.store.configured_alarm.AlarmsStore
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
                observer.onNext(AlarmDetailContract.Intent.RequestTime)
        }
    }

    override fun openTimeSelection(state: UiData) {
        view.showTimePicker(state.time ?: Time(8, 0))
    }

    override fun closeView() {
        view.finish()
    }
}
