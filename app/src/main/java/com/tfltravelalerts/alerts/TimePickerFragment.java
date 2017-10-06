
package com.tfltravelalerts.alerts;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.tfltravelalerts.alerts.events.AlertTimeSelected;
import com.tfltravelalerts.model.Time;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    private static final String LOG_TAG = "TimePickerFragment";

    private Time mInitialTime;

    public static TimePickerFragment newInstance(Time initialTime) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable(Time.class.getName(), initialTime);
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mInitialTime = args.getParcelable(Time.class.getName());
        if (mInitialTime == null) {
            Log.d(LOG_TAG, "onCreateDialog: creating time from current time");
            final Calendar c = Calendar.getInstance();
            mInitialTime = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this,
                mInitialTime.getHour(),
                mInitialTime.getMinute(),
                DateFormat.is24HourFormat(getActivity()));

        // we need to do the following bit to avoid a bug where the cancel
        // button doesn't do anything
        // http://stackoverflow.com/questions/11444238/jelly-bean-datepickerdialog-is-there-a-way-to-cancel
        timePickerDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == Dialog.BUTTON_NEGATIVE) {
                            dismissAllowingStateLoss();
                        }
                    }
                });

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        Log.d(LOG_TAG, "onTimeSet: " + hour + " " + minute);
        EventBus.getDefault().post(new AlertTimeSelected(new Time(hour, minute)));
        dismissAllowingStateLoss();
    }

}
