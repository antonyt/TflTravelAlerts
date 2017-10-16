package com.tfltravelalerts.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.tfltravelalerts.R

enum class Status constructor(
        @param:StringRes val statusResId: Int,
        @param:DrawableRes val statusIcon: Int) {
    GOOD_SERVICE(R.string.line_status_good_service, R.drawable.line_status_good_service),
    MINOR_DELAYS(R.string.line_status_minor_delays, R.drawable.line_status_minor_delays),
    SEVERE_DELAYS(R.string.line_status_severe_delays, R.drawable.line_status_severe_delays),
    PART_CLOSURE(R.string.line_status_part_closure, R.drawable.line_status_interruptions),
    PLANNED_CLOSURE(R.string.line_status_planned_closure, R.drawable.line_status_interruptions),
    SUSPENDED(R.string.line_status_suspended, R.drawable.line_status_interruptions),
    PART_SUSPENDED(R.string.line_status_part_suspended, R.drawable.line_status_interruptions),
    REDUCED_SERVICE(R.string.line_status_reduced_service, R.drawable.line_status_minor_delays),
    BUS_SERVICE(R.string.line_status_bus_service, R.drawable.line_status_interruptions),
    SERVICE_CLOSED(R.string.line_status_service_closed, R.drawable.line_status_service_closed),
    UNKNOWN(R.string.line_status_unknown, R.drawable.line_status_unknown);

    val isGoodService: Boolean
        get() = this == GOOD_SERVICE

    companion object {

        fun parseString(status: String?): Status {
            if (status == null) {
                return UNKNOWN
            } else {
                try {
                    return valueOf(status)
                } catch (e: IllegalArgumentException) {
                    return UNKNOWN
                }

            }
        }
    }
}