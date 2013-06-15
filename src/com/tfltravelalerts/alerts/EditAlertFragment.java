
package com.tfltravelalerts.alerts;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.CheckBox;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.Toast;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tfltravelalerts.R;
import com.tfltravelalerts.alerts.events.AddOrUpdateAlertRequest;
import com.tfltravelalerts.alerts.events.AlertTimeSelected;
import com.tfltravelalerts.alerts.events.AlertsUpdatedEvent;
import com.tfltravelalerts.alerts.events.DeleteAlertRequest;
import com.tfltravelalerts.alerts.events.ValidateAlertRequest;
import com.tfltravelalerts.alerts.events.ValidateAlertResult;
import com.tfltravelalerts.common.eventbus.EventBusFragment;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.Time;

public class EditAlertFragment extends EventBusFragment {

    public static final String ALERT_ID_KEY = "alertId";
    private static final String LOG_TAG = "EditAlertFragment";

    private int mAlertId;
    private LineStatusAlert mAlert;

    @InjectView(R.id.alert_title) EditText mAlertTitle;
    @InjectView(R.id.day_selector_view) DaySelectorView mDaySelectorView;
    @InjectView(R.id.line_selector_view) LineSelectorView mLineSelectorView;
    @InjectView(R.id.time_input) TimeInputField mTimeInputField;
    @InjectView(R.id.cancel_button) Button mCancelButton;
    @InjectView(R.id.save_button) Button mSaveButton;
    @InjectView(R.id.only_notify_for_disruptions_view) CheckBox mOnlyNotifyForDisruptionsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveArgs();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.alerts_add_edit_menu, menu);
        menu.findItem(R.id.delete).setVisible(mAlertId != -1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            deleteAlert();
            finishActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrieveArgs() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mAlertId = bundle.getInt(ALERT_ID_KEY, -1);
        } else {
            mAlertId = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.edit_alert_fragment, container, false);
        Views.inject(this, mRoot);
        return mRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Views.reset(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViews();

        AlertsUpdatedEvent event = (AlertsUpdatedEvent) getEventBus().getStickyEvent(
                AlertsUpdatedEvent.class);
        if (event != null) {
            onEventMainThread(event);
        }
    }

    @Override
    protected boolean registerSticky() {
        return false;
    }

    public void onEventMainThread(AlertTimeSelected newTime) {
        mTimeInputField.setTime(newTime.getData());
    }

    private void updateDays() {
        mDaySelectorView.setSelectedDays(mAlert.getDays());
    }

    private void updateLines() {
        mLineSelectorView.setSelectedLines(mAlert.getLines());
    }

    private void updateTime() {
        mTimeInputField.setTime(mAlert.getTime());
    }

    private void updateOnlyNotifyForDisruptions() {
        mOnlyNotifyForDisruptionsView.setChecked(mAlert.onlyNotifyForDisruptions());
    }

    private void setupViews() {
        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        mSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlert();
            }
        });
        mTimeInputField.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "timeInputField.onClick");
                showTimePickerDialog();
            }
        });
    }

    private void updateAlert() {
        LineStatusAlert alert = buildAlertOnScreen();

        ValidateAlertRequest request = new ValidateAlertRequest(alert);
        getEventBus().post(request);
    }

    private LineStatusAlert buildAlertOnScreen() {
        LineStatusAlert alert = LineStatusAlert.builder(mAlertId)
                .title(mAlertTitle.getText().toString())
                .clearDays()
                .addDays(mDaySelectorView.getSelectedDays())
                .clearLines()
                .addLine(mLineSelectorView.getSelectedLines())
                .setTime(mTimeInputField.getTime())
                .setOnlyNotifyForDisruptions(mOnlyNotifyForDisruptionsView.isChecked())
                .build();
        return alert;
    }

    private void deleteAlert() {
        DeleteAlertRequest request = new DeleteAlertRequest(mAlert);
        getEventBus().post(request);
    }

    private void finishActivity() {
        getActivity().finish();
    }

    private void updateTitle() {
        mAlertTitle.setText(mAlert.getTitle());
    }

    public void onEventMainThread(ValidateAlertResult event) {
        switch (event.getValidationResult()) {
            case NO_DAYS:
            case NO_LINES:
            case NO_TIME:
            case NO_TITLE:
                Toast.makeText(getActivity(), event.getValidationResult().getMessageResId(),
                        Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                AddOrUpdateAlertRequest request = new AddOrUpdateAlertRequest(event.getAlert());
                getEventBus().post(request);
                finishActivity();
                break;
        }
    }

    public void onEventMainThread(AlertsUpdatedEvent event) {
        if (mAlert == null) {
            // if we already have an alert it was restored from before
            // and we don't want to overwrite it with the one saved
            LineStatusAlert alert = event.getData().getAlertById(mAlertId);
            if (alert != null) {
                mAlert = alert;
                updateUiFromAlert();
            }
        }
    }

    private void updateUiFromAlert() {
        updateTitle();
        updateDays();
        updateLines();
        updateTime();
        updateOnlyNotifyForDisruptions();
    }

    private void showTimePickerDialog() {
        Time initialTime = mTimeInputField.getTime();
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(initialTime);
        timePickerFragment.show(getFragmentManager());
    }
}
