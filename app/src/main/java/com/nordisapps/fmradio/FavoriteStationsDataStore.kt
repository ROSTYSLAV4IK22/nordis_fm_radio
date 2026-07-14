package com.nordisapps.fmradio

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.favoriteStationsDataStore by preferencesDataStore(name = "favorite_stations")

class FavoriteStationsDataStore(private val context: Context) {
    companion object {
        private val FAVORITE_STATIONS_KEY = stringSetPreferencesKey("favorite_stations")
    }

    suspend fun getFavoriteStations(): Set<Double> {
        val prefs = context.favoriteStationsDataStore.data.first()
        val stringSet = prefs[FAVORITE_STATIONS_KEY] ?: emptySet()
        return stringSet.mapNotNull { it.toDoubleOrNull() }.toSet()
    }

    suspend fun saveFavoriteStations(stations: Set<Double>) {
        context.favoriteStationsDataStore.edit { prefs ->
            prefs[FAVORITE_STATIONS_KEY] = stations.map { it.toString() }.toSet()
        }
    }
}