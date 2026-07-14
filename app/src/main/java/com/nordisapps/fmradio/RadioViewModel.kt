package com.nordisapps.fmradio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RadioViewModel : ViewModel() {
    var uiState by mutableStateOf(RadioUiState())
        private set

    fun updateStationName(stationName: String) {
        uiState = uiState.copy(
            stationName = stationName
        )
    }

    fun updateRadioText(radioText: String) {
        uiState = uiState.copy(
            radioText = radioText
        )
    }

    fun clearRds() {
        uiState = uiState.copy(
            stationName = "",
            radioText = ""
        )
    }

    fun updateCurrentFrequency(frequency: String) {
        uiState = uiState.copy(
            currentFrequency = frequency
        )
    }

    fun updatePlaying(isPlaying: Boolean) {
        uiState = uiState.copy(
            isPlaying = isPlaying
        )
    }

    fun updateSpeakerOn(isSpeakerOn: Boolean) {
        uiState = uiState.copy(
            isSpeakerOn = isSpeakerOn
        )
    }

    fun updateScanning(isScanning: Boolean) {
        uiState = uiState.copy(
            isScanning = isScanning
        )
    }

    fun updateScannedStations(stations: List<Double>) {
        uiState = uiState.copy(scannedStations = stations)
    }

    fun updateSavedStations(stations: List<Double>) {
        uiState = uiState.copy(savedStations = stations)
    }

    fun updateShowScannedStations(show: Boolean) {
        uiState = uiState.copy(showScannedStations = show)
    }
}