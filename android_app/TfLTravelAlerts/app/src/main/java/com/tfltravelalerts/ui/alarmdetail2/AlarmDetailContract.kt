package com.tfltravelalerts.ui.alarmdetail2

import android.support.v4.app.Fragment
import com.tfltravelalerts.model.Day
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.Time
import com.tfltravelalerts.ui.alarmdetail.UiData
import io.reactivex.Observable

typealias AlarmDetailStateMachine = com.tfltravelalerts.common.StateMachine<UiData, AlarmDetailContract.Intent>

interface AlarmDetailContract {


    interface Host {
        fun showFragment(tag: String, fragment: Fragment)
    }

    interface View {
        fun init(data: UiData): Observable<Intent>

        fun render(data: UiData)

        fun showTimePicker(time: Time)
    }

    interface Presenter : UiInteractions {
        // TODO this contract probably could have an improved UiData class
        fun init(initialData: UiData, view: View)

        fun onTimeSelected(time: Time)
    }

    interface UiInteractions {
        fun save(): UiData
        fun openTimeSelection(): UiData
    }

    // TODO UiData could belong here

    sealed class Intent {
        // things happening in the UI that trigger data changes
        // TODO what about external things such as timers, push notifications, responses from
        // server, etc?
        // - maybe dealt by the presenter?
        data class LineSelection(val line: Line, val selected: Boolean) : Intent()

        data class DaySelection(val day: Day, val selected: Boolean) : Intent()
        data class NotifyGoodService(val doNotify: Boolean) : Intent()

        // TODO should these two receive ui data? or just use the state machine?
        object Save : Intent()

        object OpenTimeSelection : Intent()

        data class OnTimeSelected(val time: Time) : Intent()
    }
}
