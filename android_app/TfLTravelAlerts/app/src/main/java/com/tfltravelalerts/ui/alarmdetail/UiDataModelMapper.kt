package com.tfltravelalerts.ui.alarmdetail

import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Time

class UiDataModelMapper {
    sealed class MapperResult {
        data class Success(val configuredAlarm: ConfiguredAlarm) : MapperResult()
        object Fail : MapperResult()
    }

    private fun validate(uiData: UiData) =
            uiData.time != null

    fun map(uiData: UiData) =
            if (validate(uiData)) {
                MapperResult.Success(
                        ConfiguredAlarm(
                                uiData.id,
                                uiData.time!!.let { Time(it.hour, it.minute) },
                                uiData.days,
                                uiData.lines,
                                uiData.notifyGoodService,
                                uiData.enabled
                        )
                )
            } else {
                MapperResult.Fail
            }
}
