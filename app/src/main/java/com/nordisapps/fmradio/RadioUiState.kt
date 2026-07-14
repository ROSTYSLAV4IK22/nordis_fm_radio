package com.nordisapps.fmradio

data class RadioUiState(
    val stationName: String = "",
    val radioText: String = "",
    val currentFrequency: String = "",
    val isPlaying: Boolean = false,
    val isSpeakerOn: Boolean = false,
    val isScanning: Boolean = false,
    val scannedStations: List<Double> = emptyList(),
    val savedStations: List<Double> = emptyList(),
    val showScannedStations: Boolean = false
)