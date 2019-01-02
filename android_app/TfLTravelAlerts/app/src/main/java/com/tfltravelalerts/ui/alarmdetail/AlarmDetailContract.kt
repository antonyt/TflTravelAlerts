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

        fun render(data: UiData)

        fun getIntents(): Observable<Intent>

        // TODO view should only have two methods: get intents and get render view
        fun showTimePicker(time: Time)

        fun finish()
    }

    interface Interactions {
        fun save(state: UiData)
        fun openTimeSelection(state: UiData)
        fun closeView()
    }

    sealed class Intent {
        data class LineSelection(val line: Line, val selected: Boolean) : Intent()

        data class DaySelection(val day: Day, val selected: Boolean) : Intent()

        data class NotifyGoodService(val doNotify: Boolean) : Intent()

        data class ErrorUpdated(val message: String) : Intent()

        data class OnTimeSelected(val time: Time) : Intent()

        object CloseView : Intent()

        // TODO should these two receive ui data? or just use the state machine?
        object Save : Intent()

        object OpenTimeSelection : Intent()
    }
}
