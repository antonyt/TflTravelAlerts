package com.tfltravelalerts.ui.alarmdetail

class AlarmDetailStateReducer : AlarmDetailContract.Reducer {

    override fun reduce(currentState: UiData, event: AlarmDetailContract.Intent): UiData {
        return when (event) {
            is AlarmDetailContract.Intent.LineSelection ->
                currentState.cloneWithLine(event.line, event.selected)
            is AlarmDetailContract.Intent.DaySelection ->
                currentState.cloneWithDay(event.day, event.selected)
            is AlarmDetailContract.Intent.NotifyGoodService ->
                currentState.copy(notifyGoodService = event.doNotify)
            is AlarmDetailContract.Intent.OnTimeSelected ->
                currentState.copy(requestTime = false, time = event.time)
            is AlarmDetailContract.Intent.RequestTime ->
                currentState.copy(requestTime = true)
            AlarmDetailContract.Intent.Save,
            AlarmDetailContract.Intent.OpenTimeSelection,
            AlarmDetailContract.Intent.CloseView ->
                currentState
        }
    }
}
