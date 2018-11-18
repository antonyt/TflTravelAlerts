package com.tfltravelalerts.ui.alarmdetail

class AlarmDetailStateReducerImpl(
        initialState: UiData,
        private val uiInteractions: AlarmDetailContract.UiInteractions
) : AlarmDetailStateMachine(initialState) {

    override fun reduceState(currentState: UiData, event: AlarmDetailContract.Intent): UiData {
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
                uiInteractions.save()
            AlarmDetailContract.Intent.OpenTimeSelection ->
                uiInteractions.openTimeSelection()
        }
    }
}
