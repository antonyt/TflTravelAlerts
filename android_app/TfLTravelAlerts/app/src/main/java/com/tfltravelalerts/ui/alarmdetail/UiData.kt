package com.tfltravelalerts.ui.alarmdetail

import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Day
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.Time

data class UiData(val id: Int, val time: Time?, val days: Set<Day>, val lines: Set<Line>, val notifyGoodService: Boolean, val enabled: Boolean, val errorMessage: String?) {

    constructor(alarm: ConfiguredAlarm) : this(alarm.id, alarm.time, alarm.days, alarm.lines, alarm.notifyGoodService, alarm.enabled, null)

    constructor() : this(ConfiguredAlarm.NEW_ALARM_ID, null, emptySet(), emptySet(), false, true, null)

    val alarm: ConfiguredAlarm by lazy { ConfiguredAlarm(id, time!!, days, lines, notifyGoodService, enabled) }

    val isNewAlarm = this.id == ConfiguredAlarm.NEW_ALARM_ID

    fun cloneWithTime(time: Time): UiData {

        return copy(time = time)
    }

    fun cloneWithDay(day: Day, included: Boolean): UiData {
        return copy(days = addOrRemove(days, day, included))
    }

    fun cloneWithLine(line: Line, included: Boolean): UiData {
        return copy(lines = addOrRemove(lines, line, included))
    }

    fun cloneWithNotifyGoodService(notifyGoodService: Boolean): UiData {
        return copy(notifyGoodService = notifyGoodService)
    }

    fun cloneWithErrorMessage(errorMessage: String?): UiData {
        return copy(errorMessage = errorMessage)
    }

    private fun <T> addOrRemove(set: Set<T>, item: T, add: Boolean): Set<T> =
            if (add)
                set.plus(item)
            else
                set.minus(item)
}
