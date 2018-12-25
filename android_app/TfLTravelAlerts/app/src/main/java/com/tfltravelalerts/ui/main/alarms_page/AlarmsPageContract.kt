package com.tfltravelalerts.ui.main.alarms_page

import com.tfltravelalerts.model.ConfiguredAlarm
import io.reactivex.Observable


interface AlarmsPageContract {

    interface View {
        // should contemplate loading and possible errors?
        fun render(alarms: List<ConfiguredAlarm>)

        fun getIntents(): Observable<Intent>
    }

    interface Interactions {

        fun openAlarmDetail(alarm: ConfiguredAlarm)

        fun createAlarm()

        fun saveAlarm(alarm: ConfiguredAlarm)
    }

    abstract class StateMachine(initialState: List<ConfiguredAlarm>)
        : com.tfltravelalerts.common.StateMachine<List<ConfiguredAlarm>, Intent>(initialState)

    sealed class Intent {

        data class OpenAlarm(val alarm: ConfiguredAlarm) : Intent()

        data class ToggleAlarm(val alarm: ConfiguredAlarm, val isEnabled: Boolean) : Intent()

        data class AlarmsUpdated(val alarms: List<ConfiguredAlarm>) : Intent()

        object CreateAlarm : Intent()
    }
}