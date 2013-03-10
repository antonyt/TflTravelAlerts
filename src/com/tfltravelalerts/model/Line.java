
package com.tfltravelalerts.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.google.common.base.Joiner;
import com.tfltravelalerts.R;
import com.tfltravelalerts.TflApplication;

/**
 * London Underground line.
 */
public enum Line {
    BAKERLOO(1, R.string.line_name_bakerloo, R.color.line_color_bakerloo),

    CENTRAL(2, R.string.line_name_central, R.color.line_color_central),

    CIRCLE(7, R.string.line_name_circle, R.color.line_color_circle),

    DISTRICT(9, R.string.line_name_district, R.color.line_color_district),

    HAMMERSMITH_AND_CITY(8, R.string.line_name_hammersmith_and_city,
            R.color.line_color_hammersmith_and_city),

    JUBILEE(4, R.string.line_name_jubilee, R.color.line_color_jubilee),

    METROPOLITAN(11, R.string.line_name_metropolitan, R.color.line_color_metropolitan),

    NORTHERN(5, R.string.line_name_northern, R.color.line_color_northern),

    PICCADILLY(6, R.string.line_name_piccadilly, R.color.line_color_piccadilly),

    VICTORIA(3, R.string.line_name_victoria, R.color.line_color_victoria),

    WATERLOO_AND_CITY(12, R.string.line_name_waterloo_and_city,
            R.color.line_color_waterloo_and_city),

    OVERGROUND(82, R.string.line_name_overground, R.color.line_color_overground),

    DLR(81, R.string.line_name_dlr, R.color.line_color_dlr);

    private int mId;
    private int mNameResId;
    private int mColorResId;

    private Line(int id, int nameResId, int colorResId) {
        mId = id;
        mNameResId = nameResId;
        mColorResId = colorResId;
    }

    public static Line getLineById(int id) {
        for (Line line : values()) {
            if (line.getId() == id) {
                return line;
            }
        }
        return null;
    }

    public int getId() {
        return mId;
    }

    public int getNameResId() {
        return mNameResId;
    }

    public int getColorResId() {
        return mColorResId;
    }
    
    @Override
    public String toString() {
        Context context = TflApplication.getLastInstance();
        return context.getString(getNameResId());
    }
    
    public static String buildString(Collection<Line> lines) {
        List<Line> sortedLines = new ArrayList<Line>(lines);
        Collections.sort(sortedLines);
        return Joiner.on(", ").join(sortedLines);
    }
}
