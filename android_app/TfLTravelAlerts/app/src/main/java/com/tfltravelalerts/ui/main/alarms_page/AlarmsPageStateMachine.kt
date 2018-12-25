package com.tfltravelalerts.ui.main.alarms_page

import com.tfltravelalerts.model.ConfiguredAlarm

class AlarmsPageStateMachine(
        initialState: List<ConfiguredAlarm>,
        private val interactions: AlarmsPageContract.Interactions
) : AlarmsPageContract.StateMachine(initialState) {
    override fun reduceState(currentState: List<ConfiguredAlarm>, event: AlarmsPageContract.Intent): List<ConfiguredAlarm> {
        return when (event) {
            is AlarmsPageContract.Intent.OpenAlarm -> {
                interactions.openAlarmDetail(event.alarm)
                currentState
            }
            is AlarmsPageContract.Intent.ToggleAlarm -> {
                val updatedAlarm = event.alarm.copy(enabled = event.isEnabled)
                // TODO what if we needed to emit another event after this save alarm?
                // if saveAlarm had a "in progress + done / success" events we could deal with that
                // better
                interactions.saveAlarm(updatedAlarm)
                // TODO in order to allow the switch to animate after the user interaction,
                // we need not to update the state right now (save alarm issues an update anyway)
//                currentState.map {
//                    if (it == event.alarm) {
//                        updatedAlarm
//                    } else {
//                        it
//                    }
//                }
                currentState
            }
            is AlarmsPageContract.Intent.AlarmsUpdated -> event.alarms
            AlarmsPageContract.Intent.CreateAlarm -> {
                interactions.createAlarm()
                currentState
            }
        }
    }
}
