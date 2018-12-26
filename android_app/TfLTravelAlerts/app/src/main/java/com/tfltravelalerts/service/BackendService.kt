package com.tfltravelalerts.service

import com.tfltravelalerts.model.LineStatus
import retrofit2.Call
import retrofit2.http.GET



interface BackendService {
    @GET("get-line-status")
    fun getLiveNetworkStatus(): Call<List<LineStatus>>

    @GET("get-weekend-status")
    fun getWeekendNetworkStatus(): Call<List<LineStatus>>

    companion object {
        const val BASE_URL = "http://tfl-travel-alerts.appspot.com"
    }
}
