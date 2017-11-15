package com.tfltravelalerts.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tfltravelalerts.BuildConfig
import com.tfltravelalerts.json.LineJsonTypeAdapter
import com.tfltravelalerts.json.StatusJsonTypeAdapter
import com.tfltravelalerts.model.Line
import com.tfltravelalerts.model.LineStatus
import com.tfltravelalerts.model.Status
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
            // dependency injection here would be nice
            val gson = gson()
            val client = okHttpClient()
            val gsonConverterFactory = gsonConverterFactory(gson)
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(gsonConverterFactory)
                    .build()
                    .create(BackendService::class.java)
        }

        private fun gson(): Gson {
            return GsonBuilder()
                    .registerTypeAdapter(Line::class.java, LineJsonTypeAdapter())
                    .registerTypeAdapter(Status::class.java, StatusJsonTypeAdapter())
                    .create()
        }

        private fun gsonConverterFactory(gson: Gson) = GsonConverterFactory.create(gson)


        private fun okHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }
    }
}