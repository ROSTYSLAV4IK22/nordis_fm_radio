package com.nordisapps.fmradio.ui

import androidx.compose.runtime.Composable
import com.nordisapps.fmradio.RadioViewModel

@Composable
fun RadioHomeScreen(viewModel: RadioViewModel) {
    val uiState = viewModel.uiState

    RadioPlayerScreen(
        currentFrequency = uiState.currentFrequency,
        stationName = uiState.stationName,
        radioText = uiState.radioText,
        isPlaying = uiState.isPlaying,
        isScanning = uiState.isScanning,
        isSpeakerOn = uiState.isSpeakerOn,
        isRecording = uiState.isRecording,
        isRecordingPaused = uiState.isRecordingPaused,
        recordingTime = uiState.recordingTime,
        scannedStations = uiState.scannedStations,
        savedStations = uiState.savedStations,
        favoriteStations = uiState.favoriteStations,
        showScannedStations = uiState.showScannedStations,
        onSeekDownClick = viewModel::seekDown,
        onSeekUpClick = viewModel::seekUp,
        onPowerClick = viewModel::togglePower,
        onSpeakerClick = viewModel::toggleSpeaker,
        onScaleFrequencyChange = viewModel::onScaleFrequencyChange,
        onScanClick = viewModel::startScan,
        onConfirmScannedStations = viewModel::confirmScannedStations,
        onSavedStationSelected = viewModel::tuneToStation,
        onFavoriteToggle = viewModel::toggleFavoriteAndPersist,
        onRecordClick = viewModel::onRecordClick,
        onRecordPauseClick = viewModel::onRecordPauseClick,
        onRecordStopClick = viewModel::onRecordStopClick,
        onRecordCancelClick = viewModel::onRecordCancelClick
    )
}