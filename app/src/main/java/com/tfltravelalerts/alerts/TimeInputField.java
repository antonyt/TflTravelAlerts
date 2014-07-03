
package com.tfltravelalerts.alerts;

import android.widget.TextView;

import android.content.Context;
import android.util.AttributeSet;

import com.tfltravelalerts.R;
import com.tfltravelalerts.model.Time;

public class TimeInputField extends TextView {

    public TimeInputField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TimeInputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeInputField(Context context) {
        super(context);
        init();
    }

    private void init() {
        setFreezesText(true);
        setHint(R.string.edit_alert_time_field_hint);
    }

    public void setTime(Time time) {
        if (time == null) {
            setText("");
        } else {
            setText(time.toString());
        }
    }

    public Time getTime() {
        return Time.parseTime(getText().toString());
    }

}
