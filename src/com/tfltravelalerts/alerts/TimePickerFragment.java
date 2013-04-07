
package com.tfltravelalerts.alerts;

import java.util.Calendar;

import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.TimePickerDialog;
import org.holoeverywhere.widget.TimePicker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.tfltravelalerts.alerts.events.AlertTimeSelected;
import com.tfltravelalerts.model.Time;

import de.greenrobot.event.EventBus;

public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    private final String LOG_TAG = "TimePickerFragment";
    private int mHour = -1;
    private int mMinute;
    private TimePickerDialog mTimePickerDialog;

    public void setInitialTime(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Log.d(LOG_TAG, "onCreateDialog");
        if (mHour == -1) {
            Log.d(LOG_TAG, "onCreateDialog: creating time from current time");
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
        }
        if (mTimePickerDialog == null) {
            Log.d(LOG_TAG, "onCreateDialog: instanciating a new dialog");
            mTimePickerDialog = new TimePickerDialog(getActivity(), this, mHour, mMinute,
                    DateFormat.is24HourFormat(getActivity()));

            // we need to do the following bit to avoid a bug where the cancel
            // button doesn't
            // do anything
            // http://stackoverflow.com/questions/11444238/jelly-bean-datepickerdialog-is-there-a-way-to-cancel
            mTimePickerDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == Dialog.BUTTON_NEGATIVE) {
                                dismissAllowingStateLoss();
                            }
                        }
                    });
        } else {
            Log.d(LOG_TAG, "onCreateDialog: already had a dialog, just updating time");
            mTimePickerDialog.updateTime(mHour, mMinute);
        }
        return mTimePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        Log.d(LOG_TAG, "onTimeSet: " + hour + " " + minute);
        mHour = hour;
        mMinute = minute;
        EventBus.getDefault().postSticky(new AlertTimeSelected(new Time(mHour, mMinute)));
        dismissAllowingStateLoss();
    }

}
