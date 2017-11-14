package com.tfltravelalerts.service

import com.tfltravelalerts.BuildConfig
import com.tfltravelalerts.model.LineStatus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


const val BASE_URL = "http://tfl-travel-alerts.appspot.com"

interface BackendService {
    @GET("get-line-status")
    fun getLiveNetworkStatus(): Call<List<LineStatus>>

    @GET("get-weekend-status")
    fun getWeekendNetworkStatus(): Call<List<LineStatus>>

    companion object {
        fun createService(): BackendService {
            val client = okHttpClient()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(BackendService::class.java)
        }

        private fun okHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }
    }
}