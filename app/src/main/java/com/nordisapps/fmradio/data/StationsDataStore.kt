package com.nordisapps.fmradio.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.stationsDataStore by preferencesDataStore(name = "saved_stations")

class StationsDataStore(private val context: Context) {
    companion object {
        private val SAVED_STATIONS_KEY = stringSetPreferencesKey("saved_stations")
    }

    suspend fun getSavedStations(): List<Double> {
        val prefs = context.stationsDataStore.data.first()
        val stringSet = prefs[SAVED_STATIONS_KEY] ?: emptySet()
        return stringSet.mapNotNull { it.toDoubleOrNull() }.sorted()
    }

    suspend fun saveStations(stations: List<Double>) {
        context.stationsDataStore.edit { prefs ->
            prefs[SAVED_STATIONS_KEY] = stations.map { it.toString() }.toSet()
        }
    }
}