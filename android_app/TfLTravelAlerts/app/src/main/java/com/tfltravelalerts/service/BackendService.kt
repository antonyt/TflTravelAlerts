package com.tfltravelalerts.service

import com.tfltravelalerts.model.LineStatus
import io.reactivex.Single
import retrofit2.http.GET



interface BackendService {
    @GET("get-line-status")
    fun getLiveNetworkStatus(): Single<List<LineStatus>>

    @GET("get-weekend-status")
    fun getWeekendNetworkStatus(): Single<List<LineStatus>>

    companion object {
        const val BASE_URL = "http://tfl-travel-alerts.appspot.com"
    }
}
