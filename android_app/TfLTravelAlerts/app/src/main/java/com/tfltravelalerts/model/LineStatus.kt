package com.tfltravelalerts.model
                 // @SerializedName("line")
                 // @SerializedName("state")
                 // @SerializedName("details")
class LineStatus(val line: Line,
                 val status: Status,
                 val description: String) {

    fun hasChanges(other: LineStatus?): Boolean {
        return other == null
                || line != other.line
                || description != other.description
                || status != other.status
    }

    val isGoodService: Boolean
        get() = this.status.isGoodService

}