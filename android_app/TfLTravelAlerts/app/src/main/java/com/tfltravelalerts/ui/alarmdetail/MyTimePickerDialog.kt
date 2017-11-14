package com.tfltravelalerts.ui.alarmdetail

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.widget.TimePicker
import com.tfltravelalerts.common.Assertions
import com.tfltravelalerts.common.Logger
import com.tfltravelalerts.model.Time

interface MyTimePickerListener {
    fun onTimeSelected(time: Time)
}

class MyAlarmTimePickerDialog : DialogFragment(), OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): TimePickerDialog {
        checkHostInterface()
        val args = arguments!!
        val h = args.getInt(ARG_HOUR)
        val m = args.getInt(ARG_MINUTE)
        val dialog = TimePickerDialog(context, this, h, m, DateFormat.is24HourFormat(activity))
        dialog.setTitle(null)
        return dialog
    }

    private fun checkHostInterface() {
        if (activity !is MyTimePickerListener) {
            Assertions.shouldNotHappen("Activity doesn't implement required interface")
        }
    }

    override fun onTimeSet(timePicker: TimePicker, hour: Int, minute: Int) {
        Logger.d("onTimeSet: $hour:$minute")
        fragmentManager?.popBackStack()
        val activity = activity
        if (activity is MyTimePickerListener) {
            activity.onTimeSelected(Time(hour, minute))
        } else {
            Assertions.shouldNotHappen("activity not instance of AlarmDetailActivity")
        }
    }

    companion object {
        private val ARG_HOUR = "h"
        private val ARG_MINUTE = "m"

        fun create(time: Time): MyAlarmTimePickerDialog {
            val dialog = MyAlarmTimePickerDialog()
            val args = Bundle()
            args.putInt(ARG_HOUR, time.hour)
            args.putInt(ARG_MINUTE, time.minute)
            dialog.arguments = args
            return dialog
        }
    }
}