package com.tfltravelalerts.ui.alarmdetail

class AlarmDetailStateReducerImpl(
        private val uiInteractions: AlarmDetailContract.UiInteractions
) : AlarmDetailContract.Reducer {

    override fun reduce(currentState: UiData, event: AlarmDetailContract.Intent): UiData {
        return when (event) {
            is AlarmDetailContract.Intent.LineSelection ->
                currentState.cloneWithLine(event.line, event.selected)
            is AlarmDetailContract.Intent.DaySelection ->
                currentState.cloneWithDay(event.day, event.selected)
            is AlarmDetailContract.Intent.NotifyGoodService ->
                currentState.cloneWithNotifyGoodService(event.doNotify)
            is AlarmDetailContract.Intent.OnTimeSelected ->
                currentState.cloneWithTime(event.time)
            AlarmDetailContract.Intent.Save ->
                uiInteractions.save(currentState)
            AlarmDetailContract.Intent.OpenTimeSelection ->
                uiInteractions.openTimeSelection(currentState)
        }
    }
}
