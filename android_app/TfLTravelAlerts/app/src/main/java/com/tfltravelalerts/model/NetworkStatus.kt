package com.tfltravelalerts.model

import java.util.*

data class NetworkStatus(
        val date: Date,
        val lineStatus: List<LineStatus>
)
