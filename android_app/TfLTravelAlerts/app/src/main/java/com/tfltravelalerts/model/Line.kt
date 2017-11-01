package com.tfltravelalerts.model

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import com.tfltravelalerts.R


enum class Line(@StringRes val nameResId: Int,
                @ColorRes val colorResId: Int,
                val isDarkBackground: Boolean) {
    BAKERLOO(R.string.line_name_bakerloo, R.color.line_color_bakerloo, true),
    CENTRAL(R.string.line_name_central, R.color.line_color_central, true),
    CIRCLE(R.string.line_name_circle, R.color.line_color_circle, false),
    DISTRICT(R.string.line_name_district, R.color.line_color_district, true),
    HAMMERSMITH_AND_CITY(R.string.line_name_hammersmith_and_city, R.color.line_color_hammersmith_and_city, true),
    JUBILEE(R.string.line_name_jubilee, R.color.line_color_jubilee, true),
    METROPOLITAN(R.string.line_name_metropolitan, R.color.line_color_metropolitan, true),
    NORTHERN(R.string.line_name_northern, R.color.line_color_northern, true),
    PICCADILLY(R.string.line_name_piccadilly, R.color.line_color_piccadilly, true),
    VICTORIA(R.string.line_name_victoria, R.color.line_color_victoria, true),
    WATERLOO_AND_CITY(R.string.line_name_waterloo_and_city, R.color.line_color_waterloo_and_city, true),
    OVERGROUND(R.string.line_name_overground, R.color.line_color_overground, true),
    DLR(R.string.line_name_dlr, R.color.line_color_dlr, true),
    TFL_RAIL(R.string.line_name_tfl_rail, R.color.line_color_tfl_rail, true),
    TRAMS(R.string.line_name_trams, R.color.line_color_tram, true);

    // TODO add support for tfl rail and trams

    companion object {
        fun tryParse(name: String?): Line? {
            val line: Line? = null
            if (name == null) {
                return line
            } else {
                try {
                    return valueOf(name)
                } catch (e: IllegalArgumentException) {
                    return line
                }

            }
        }
    }

}