package com.tfltravelalerts.model

import java.util.*

data class NetworkStatus(
        val date: Date,
        val lineStatus: List<LineStatus>
) {
    companion object {
        val LIVE_STATUS = NetworkStatus(
                Calendar.getInstance().time,
                listOf(
                        LineStatus(Line.BAKERLOO, Status.GOOD_SERVICE, "Good service"),
                        LineStatus(Line.CENTRAL, Status.BUS_SERVICE, "Bus service on.."),
                        LineStatus(Line.CIRCLE, Status.MINOR_DELAYS, "Minor delays"),
                        LineStatus(Line.DISTRICT, Status.SEVERE_DELAYS, "severere delaysss"),
                        LineStatus(Line.DLR, Status.SERVICE_CLOSED, "closed"),
                        LineStatus(Line.CENTRAL, Status.PLANNED_CLOSURE, "closure"),
                        LineStatus(Line.HAMMERSMITH_AND_CITY, Status.GOOD_SERVICE, "Good service"),
                        LineStatus(Line.OVERGROUND, Status.BUS_SERVICE, "Bus service on.."),
                        LineStatus(Line.NORTHERN, Status.MINOR_DELAYS, "Minor delays"),
                        LineStatus(Line.METROPOLITAN, Status.SEVERE_DELAYS, "severere delaysss"),
                        LineStatus(Line.JUBILEE, Status.SERVICE_CLOSED, "closed"),
                        LineStatus(Line.PICCADILLY, Status.PLANNED_CLOSURE, "closure")
                ))
        val WEEKEND_STATUS = NetworkStatus(
                Calendar.getInstance().time,
                listOf(
                        LineStatus(Line.HAMMERSMITH_AND_CITY, Status.GOOD_SERVICE, "Good service"),
                        LineStatus(Line.OVERGROUND, Status.BUS_SERVICE, "Bus service on.."),
                        LineStatus(Line.NORTHERN, Status.MINOR_DELAYS, "Minor delays"),
                        LineStatus(Line.METROPOLITAN, Status.SEVERE_DELAYS, "severere delaysss"),
                        LineStatus(Line.JUBILEE, Status.SERVICE_CLOSED, "closed"),
                        LineStatus(Line.PICCADILLY, Status.PLANNED_CLOSURE, "closure"),
                        LineStatus(Line.BAKERLOO, Status.GOOD_SERVICE, "Good service"),
                        LineStatus(Line.CENTRAL, Status.BUS_SERVICE, "Bus service on.."),
                        LineStatus(Line.CIRCLE, Status.MINOR_DELAYS, "Minor delays"),
                        LineStatus(Line.DISTRICT, Status.SEVERE_DELAYS, "severere delaysss"),
                        LineStatus(Line.DLR, Status.SERVICE_CLOSED, "closed"),
                        LineStatus(Line.CENTRAL, Status.PLANNED_CLOSURE, "closure")
                ))
    }
}


