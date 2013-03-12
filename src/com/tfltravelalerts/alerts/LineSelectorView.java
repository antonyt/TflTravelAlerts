
package com.tfltravelalerts.alerts;

import java.util.Set;

import org.holoeverywhere.widget.CheckBox;
import org.holoeverywhere.widget.LinearLayout;

import android.content.Context;
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
