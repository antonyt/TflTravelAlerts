package com.tfltravelalerts.model
                 // @SerializedName("line")
                 // @SerializedName("state")
                 // @SerializedName("details")
data class LineStatus(val line: Line,
                 val status: Status,
                 val description: String) {

    val isGoodService: Boolean
        get() = this.status.isGoodService

}