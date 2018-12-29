package com.tfltravelalerts.ui.alarmdetail

import android.support.v4.app.Fragment
import com.tfltravelalerts.model.Day
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.Time
import io.reactivex.Observable

interface AlarmDetailContract {

    interface Reducer : com.tfltravelalerts.common.Reducer<UiData, Intent>

    interface Host {
        fun showFragment(tag: String, fragment: Fragment)

        fun finish()
    }

    interface View {
        fun init(data: UiData): Observable<Intent>

        fun render(data: UiData)

        // TODO view should only have two methods: get intents and get render view
        fun showTimePicker(time: Time)

        fun finish()
    }

    interface Presenter : UiInteractions {
        // TODO this contract probably could have an improved UiData class
        fun init(initialData: UiData, view: View)

        fun onTimeSelected(time: Time)
    }

    interface UiInteractions {
        fun save(state: UiData): UiData
        fun openTimeSelection(state: UiData): UiData
    }

    // TODO UiData could belong here

    sealed class Intent {
        data class LineSelection(val line: Line, val selected: Boolean) : Intent()

        data class DaySelection(val day: Day, val selected: Boolean) : Intent()
        data class NotifyGoodService(val doNotify: Boolean) : Intent()

        // TODO should these two receive ui data? or just use the state machine?
        object Save : Intent()

        object OpenTimeSelection : Intent()

        data class OnTimeSelected(val time: Time) : Intent()
    }
}
