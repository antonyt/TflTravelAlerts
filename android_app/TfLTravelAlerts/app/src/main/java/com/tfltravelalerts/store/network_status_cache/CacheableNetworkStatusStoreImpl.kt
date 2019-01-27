package com.tfltravelalerts.store.network_status_cache

import android.content.SharedPreferences
import com.google.gson.Gson
import com.tfltravelalerts.model.NetworkStatus
import com.tfltravelalerts.store.network_status.NetworkStatusResponse
import io.reactivex.Maybe

private const val LIVE_STATUS = "live"
private const val WEEKEND_STATUS = "weekend"

class CacheableNetworkStatusStoreImpl(
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : CacheableNetworkStatusStore {
    /*
     * TODO Due to the nature of this cache, instead of Single this should expose Maybe
     * but there are advantages in having this implementing the same interface as the store
     *
     * TODO could create a proper interface for the cache store and then create a bridge from
     * methods with Maybe to methods with Single
     */
    override fun getLiveNetworkStatus(): Maybe<NetworkStatusResponse> =
            loadFromSharedPreferences(LIVE_STATUS)

    override fun getWeekendNetworkStatus(): Maybe<NetworkStatusResponse> =
            loadFromSharedPreferences(WEEKEND_STATUS)

    override fun saveLiveNetworkStatus(status: NetworkStatus) =
            saveToSharedPreferences(status, LIVE_STATUS)

    override fun saveWeekendNetworkStatus(status: NetworkStatus) =
            saveToSharedPreferences(status, WEEKEND_STATUS)

    private fun saveToSharedPreferences(status: NetworkStatus, sharedPreferencesKey: String) {
        val json = gson.toJson(status)
        sharedPreferences
                .edit()
                .putString(sharedPreferencesKey, json)
                .apply()
    }

    private fun loadFromSharedPreferences(sharedPreferencesKey: String): Maybe<NetworkStatusResponse> {
        sharedPreferences.getString(sharedPreferencesKey, null).run {
            return if (this != null) {
                Maybe.fromCallable {
                    gson.fromJson(this, NetworkStatus::class.java)
                }.map<NetworkStatusResponse> {
                    NetworkStatusResponse.Success(it)
                }.onErrorReturn {
                    NetworkStatusResponse.UnknownError(it)
                }
            } else {
                Maybe.empty()
            }
        }
    }
}
