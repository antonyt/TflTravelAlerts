package com.tfltravelalerts.store

import android.content.SharedPreferences
import com.google.gson.Gson
import com.tfltravelalerts.model.NetworkStatus
import io.reactivex.Maybe
import io.reactivex.Single

private const val LIVE_STATUS = "live"
private const val WEEKEND_STATUS = "weekend"

class CacheableNetworkStatusStoreImpl(
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : NetworkStatusStore, CacheableNetworkStatusStore {
    /*
     * TODO Due to the nature of this cache, instead of Single this should expose Maybe
     * but there are advantages in having this implementing the same interface as the store
     *
     * TODO could create a proper interface for the cache store and then create a bridge from
     * methods with Maybe to methods with Single
     */
    override fun getLiveNetworkStatus(): Single<NetworkStatusResponse> {
        return loadFromSharedPreferencesAsSingle(LIVE_STATUS)
    }

    override fun getWeekendNetworkStatus(): Single<NetworkStatusResponse> {
        return loadFromSharedPreferencesAsSingle(WEEKEND_STATUS)
    }

    override fun saveLiveNetworkStatus(status: NetworkStatus) {
        saveToSharedPreferences(status, LIVE_STATUS)
    }

    override fun saveWeekendNetworkStatus(status: NetworkStatus) {
        saveToSharedPreferences(status, WEEKEND_STATUS)
    }

    private fun saveToSharedPreferences(status: NetworkStatus, sharedPreferencesKey: String) {
        val json = gson.toJson(status)
        sharedPreferences
                .edit()
                .putString(sharedPreferencesKey, json)
                .apply()
    }

    private fun loadFromSharedPreferencesAsSingle(sharedPreferencesKey: String) =
            loadFromSharedPreferences(sharedPreferencesKey).toSingle()

    private fun loadFromSharedPreferences(sharedPreferencesKey: String): Maybe<NetworkStatusResponse> {
        sharedPreferences.getString(sharedPreferencesKey, null).run {
            return if (this != null) {
                Maybe.fromCallable {
                    gson.fromJson(this, NetworkStatusResponse::class.java)
                }
            } else {
                Maybe.empty()
            }
        }
    }
}
