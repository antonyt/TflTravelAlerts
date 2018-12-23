package com.tfltravelalerts.ui.main.alarms_page

import com.tfltravelalerts.model.ConfiguredAlarm

class AlarmsPageStateStateMachine(
        initialState: List<ConfiguredAlarm>
) : AlarmsPageContract.StateMachine(initialState) {
    override fun reduceState(currentState: List<ConfiguredAlarm>, event: AlarmsPageContract.Intent): List<ConfiguredAlarm> {
        return when (event) {
            is AlarmsPageContract.Intent.OpenAlarm -> currentState
            is AlarmsPageContract.Intent.ToggleAlarm -> {
                currentState.map {
                    if (it == event.alarm) {
                        it.copy(enabled = event.isEnabled)
                    } else {
                        it
                    }
                }
            }
            is AlarmsPageContract.Intent.AlarmsUpdated -> event.alarms
            AlarmsPageContract.Intent.CreateAlarm -> currentState
        }
    }
}
