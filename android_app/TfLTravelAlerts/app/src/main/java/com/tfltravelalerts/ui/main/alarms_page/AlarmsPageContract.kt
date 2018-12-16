package com.tfltravelalerts.ui.main.alarms_page

import com.tfltravelalerts.model.ConfiguredAlarm
import io.reactivex.Observable


interface AlarmsPageContract {

    interface View {
        // should contemplate loading and possible errors?
        fun render(alarms: List<ConfiguredAlarm>)

        fun getIntents(): Observable<Intent>
    }

    interface Presenter {

        fun onAlarmClicked(alarm: ConfiguredAlarm)

        fun onAlarmEnabledChanged(alarm: ConfiguredAlarm, isEnabled: Boolean)
    }

    sealed class Intent {

        data class OpenAlarm(val alarm: ConfiguredAlarm) : Intent()

        data class ToggleAlarm(val alarm: ConfiguredAlarm, val isEnabled: Boolean) : Intent()

        object CreateAlarm : Intent()
    }
}
