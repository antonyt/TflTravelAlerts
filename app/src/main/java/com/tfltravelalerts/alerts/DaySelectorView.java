
package com.tfltravelalerts.alerts;

import java.util.Set;

import android.widget.CheckBox;
import android.widget.LinearLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.tfltravelalerts.R;
import com.tfltravelalerts.common.ViewVisitor;
import com.tfltravelalerts.common.ViewVisitor.ViewVisitorCallback;
import com.tfltravelalerts.model.Day;

public class DaySelectorView extends LinearLayout {

    public DaySelectorView(Context context, AttributeSet attrs, int defStyleRes) {
        super(context, attrs, defStyleRes);
    }

    public DaySelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DaySelectorView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        findViewById(R.id.day_selector_monday).setTag(Day.MONDAY);
        findViewById(R.id.day_selector_tuesday).setTag(Day.TUESDAY);
        findViewById(R.id.day_selector_wednesday).setTag(Day.WEDNESDAY);
        findViewById(R.id.day_selector_thursday).setTag(Day.THURSDAY);
        findViewById(R.id.day_selector_friday).setTag(Day.FRIDAY);
        findViewById(R.id.day_selector_saturday).setTag(Day.SATURDAY);
        findViewById(R.id.day_selector_sunday).setTag(Day.SUNDAY);
    }

    public void setSelectedDays(Set<Day> selectedDays) {
        setSelectedDays(this, selectedDays);
    }

    private void setSelectedDays(ViewGroup root, final Set<Day> selectedDays) {
        ViewVisitor.visitAll(this, new ViewVisitorCallback() {
            @Override
            public void onViewVisited(View view) {
                if (view instanceof CheckBox && view.getTag() instanceof Day) {
                    CheckBox checkbox = (CheckBox) view;
                    Day day = (Day) checkbox.getTag();

                    checkbox.setChecked(selectedDays.contains(day));
                }
            }
        });
    }

    public ImmutableSet<Day> getSelectedDays() {
        final Builder<Day> selectedDays = ImmutableSet.<Day> builder();

        ViewVisitor.visitAll(this, new ViewVisitorCallback() {
            @Override
            public void onViewVisited(View view) {
                if (view instanceof CheckBox && view.getTag() instanceof Day) {
                    CheckBox checkbox = (CheckBox) view;
                    Day day = (Day) checkbox.getTag();

                    if (checkbox.isChecked()) {
                        selectedDays.add(day);
                    }
                }
            }
        });

        return selectedDays.build();
    }

}
