package com.tfltravelalerts.model


private val NEW_ALARM_ID = -1

data class ConfiguredAlarm(
        val time: Time,
        val days: Set<Day>,
        val lines: Set<Line>,
        val suppressNotifications: Boolean
        )