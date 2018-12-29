package com.tfltravelalerts.ui.main.alarms_page

import com.tfltravelalerts.model.ConfiguredAlarm

class AlarmsPageReducer : AlarmsPageContract.Reducer {
    override fun reduce(currentState: List<ConfiguredAlarm>, event: AlarmsPageContract.Intent): List<ConfiguredAlarm> {
        return when (event) {
            is AlarmsPageContract.Intent.OpenAlarm,
            is AlarmsPageContract.Intent.ToggleAlarm,
            AlarmsPageContract.Intent.CreateAlarm ->
                currentState
            is AlarmsPageContract.Intent.AlarmsUpdated ->
                event.alarms
        }
    }
}
