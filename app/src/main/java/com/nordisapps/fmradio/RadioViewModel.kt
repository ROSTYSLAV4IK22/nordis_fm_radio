package com.nordisapps.fmradio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RadioViewModel : ViewModel() {
    private var recordingTimerJob: Job? = null
    private var recordingSeconds = 0

    var uiState by mutableStateOf(RadioUiState())
        private set

    private fun updateRecordingTime() {
        val hours = recordingSeconds / 3600
        val minutes = (recordingSeconds % 3600) / 60
        val seconds = recordingSeconds % 60

        val formatted = if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }

        uiState = uiState.copy(recordingTime = formatted)
    }

    private fun startRecordingTimer() {
        recordingTimerJob?.cancel()

        recordingTimerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000.milliseconds)

                if (!uiState.isRecordingPaused) {
                    recordingSeconds++
                    updateRecordingTime()
                }
            }
        }
    }

    private fun stopRecordingTimer() {
        recordingTimerJob?.cancel()
        recordingTimerJob = null
        recordingSeconds = 0

        uiState = uiState.copy(
            recordingTime = "00:00"
        )
    }

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

    fun toggleFavorite(frequency: Double) {
        val current = uiState.favoriteStations
        uiState = uiState.copy(
            favoriteStations = if (current.contains(frequency)) {
                current - frequency
            } else {
                current + frequency
            }
        )
    }

    fun setFavoriteStations(stations: Set<Double>) {
        uiState = uiState.copy(favoriteStations = stations)
    }

    fun updateRecording(isRecording: Boolean) {
        uiState = uiState.copy(
            isRecording = isRecording
        )

        if (isRecording) {
            recordingSeconds = 0
            updateRecordingTime()
            startRecordingTimer()
        } else {
            stopRecordingTimer()
        }
    }

    fun updateRecordingPaused(isRecordingPaused: Boolean) {
        uiState = uiState.copy(
            isRecordingPaused = isRecordingPaused
        )
    }
}