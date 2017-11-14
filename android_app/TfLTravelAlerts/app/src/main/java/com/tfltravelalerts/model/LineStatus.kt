package com.tfltravelalerts.model

import com.google.gson.annotations.SerializedName

data class LineStatus(
        val line: Line,
        @SerializedName("state")
        val status: Status,
        @SerializedName("details")
        val description: String) {

    val isGoodService: Boolean
        get() = this.status.isGoodService

}