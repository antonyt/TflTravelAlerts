
package com.tfltravelalerts.alerts;

import java.util.Set;


import android.graphics.drawable.StateListDrawable;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.tfltravelalerts.R;
import com.tfltravelalerts.common.ViewVisitor;
import com.tfltravelalerts.common.ViewVisitor.ViewVisitorCallback;
import com.tfltravelalerts.model.Line;

/**
 * TODO: unify with {@link DaySelectorView}.
 */
public class LineSelectorView extends LinearLayout {

    private static final int[] CHECKED_STATE = {
            android.R.attr.state_checked
    };

    public LineSelectorView(Context context, AttributeSet attrs, int defStyleRes) {
        super(context, attrs, defStyleRes);
    }

    public LineSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineSelectorView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        findViewById(R.id.line_selector_bakerloo).setTag(Line.BAKERLOO);
        findViewById(R.id.line_selector_central).setTag(Line.CENTRAL);
        findViewById(R.id.line_selector_circle).setTag(Line.CIRCLE);
        findViewById(R.id.line_selector_district).setTag(Line.DISTRICT);
        findViewById(R.id.line_selector_hammersmith_and_city).setTag(Line.HAMMERSMITH_AND_CITY);
        findViewById(R.id.line_selector_jubilee).setTag(Line.JUBILEE);
        findViewById(R.id.line_selector_metropolitan).setTag(Line.METROPOLITAN);
        findViewById(R.id.line_selector_nothern).setTag(Line.NORTHERN);
        findViewById(R.id.line_selector_piccadilly).setTag(Line.PICCADILLY);
        findViewById(R.id.line_selector_victoria).setTag(Line.VICTORIA);
        findViewById(R.id.line_selector_waterloo_and_city).setTag(Line.WATERLOO_AND_CITY);
        findViewById(R.id.line_selector_overground).setTag(Line.OVERGROUND);
        findViewById(R.id.line_selector_dlr).setTag(Line.DLR);

        updateBackgroundDrawables();
    }

    private void updateBackgroundDrawables() {
        ViewVisitor.visitAll(this, new ViewVisitorCallback() {
            @Override
            public void onViewVisited(View view) {
                if (view instanceof CheckBox && view.getTag() instanceof Line) {
                    Line line = (Line) view.getTag();
                    Drawable backgroundDrawable = makeBackgroundDrawable(line);
                    view.setBackgroundDrawable(backgroundDrawable);
                }
            }
        });
    }

    private Drawable makeBackgroundDrawable(Line line) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.setExitFadeDuration(150);
        stateListDrawable.addState(CHECKED_STATE,
                makeColorDrawable(line.getColorResId()));
        stateListDrawable.addState(View.EMPTY_STATE_SET,
                makeColorDrawable(R.color.translucent_dark_gray));
        return stateListDrawable;
    }

    private ColorDrawable makeColorDrawable(int colorResId) {
        return new ColorDrawable(getResources().getColor(colorResId));
    }

    public void setSelectedLines(final Set<Line> selectedLines) {
        ViewVisitor.visitAll(this, new ViewVisitorCallback() {
            @Override
            public void onViewVisited(View view) {
                if (view instanceof CheckBox && view.getTag() instanceof Line) {
                    CheckBox checkbox = (CheckBox) view;
                    Line line = (Line) checkbox.getTag();

                    checkbox.setChecked(selectedLines.contains(line));
                }
            }
        });
    }

    public ImmutableSet<Line> getSelectedLines() {
        final Builder<Line> selectedLines = ImmutableSet.<Line> builder();

        ViewVisitor.visitAll(this, new ViewVisitorCallback() {
            @Override
            public void onViewVisited(View view) {
                if (view instanceof CheckBox && view.getTag() instanceof Line) {
                    CheckBox checkbox = (CheckBox) view;
                    Line line = (Line) checkbox.getTag();

                    if (checkbox.isChecked()) {
                        selectedLines.add(line);
                    }
                }
            }
        });

        return selectedLines.build();
    }

}
