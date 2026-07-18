package com.nordisapps.fmradio.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RadioPlayerScreen(
    currentFrequency: String,
    stationName: String,
    radioText: String,
    isPlaying: Boolean,
    isScanning: Boolean,
    isSpeakerOn: Boolean,
    scannedStations: List<Double>,
    savedStations: List<Double>,
    favoriteStations: Set<Double>,
    showScannedStations: Boolean,
    isRecording: Boolean,
    isRecordingPaused: Boolean,
    recordingTime: String,
    onScaleFrequencyChange: (Double) -> Unit,
    onSeekUpClick: () -> Unit,
    onSeekDownClick: () -> Unit,
    onPowerClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    onScanClick: () -> Unit,
    onConfirmScannedStations: () -> Unit,
    onSavedStationSelected: (Double) -> Unit,
    onFavoriteToggle: (Double) -> Unit,
    onRecordClick: () -> Unit,
    onRecordPauseClick: () -> Unit,
    onRecordStopClick: () -> Unit,
    onRecordCancelClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        PlaybackControlsRow(
            isPlaying = isPlaying,
            isRecording = isRecording,
            isRecordingPaused = isRecordingPaused,
            onPowerClick = onPowerClick,
            onRecordClick = onRecordClick,
            onRecordPauseClick = onRecordPauseClick,
            onRecordStopClick = onRecordStopClick,
            onRecordCancelClick = onRecordCancelClick,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        )

        ScanSpeakerRow(
            isPlaying = isPlaying,
            isScanning = isScanning,
            isSpeakerOn = isSpeakerOn,
            onScanClick = onScanClick,
            onSpeakerClick = onSpeakerClick,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 96.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRecording) {
                RecordingStatusText(
                    isRecordingPaused = isRecordingPaused,
                    recordingTime = recordingTime,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            FrequencyTuningRow(
                currentFrequency = currentFrequency,
                isPlaying = isPlaying,
                onSeekDownClick = onSeekDownClick,
                onSeekUpClick = onSeekUpClick,
                onFrequencyConfirmed = onScaleFrequencyChange
            )

            Text(text = stationName)
            Text(text = radioText, modifier = Modifier.padding(bottom = 12.dp))

            FrequencyScale(
                currentFrequency = currentFrequency.toFloatOrNull() ?: 87.5f,
                isPlaying = isPlaying,
                onFrequencyChange = { onScaleFrequencyChange(it.toDouble()) },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            SavedStationsSection(
                stations = savedStations,
                favoriteStations = favoriteStations,
                onStationSelected = onSavedStationSelected,
                onFavoriteToggle = onFavoriteToggle,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showScannedStations) {
        ScannedStationsSheet(stations = scannedStations, onConfirm = onConfirmScannedStations)
    }
}

@Composable
private fun PlaybackControlsRow(
    isPlaying: Boolean,
    isRecording: Boolean,
    isRecordingPaused: Boolean,
    onPowerClick: () -> Unit,
    onRecordClick: () -> Unit,
    onRecordPauseClick: () -> Unit,
    onRecordStopClick: () -> Unit,
    onRecordCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onPowerClick) {
            Icon(Icons.Default.PowerSettingsNew, contentDescription = null)
        }

        if (!isRecording) {
            IconButton(onClick = onRecordClick, enabled = isPlaying) {
                Icon(
                    imageVector = Icons.Default.FiberManualRecord,
                    tint = if (isPlaying) Color(0xFFD32F2F) else Color.Gray,
                    contentDescription = null
                )
            }
        } else {
            IconButton(onClick = onRecordPauseClick) {
                Icon(
                    imageVector = if (isRecordingPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = null
                )
            }
            IconButton(onClick = onRecordStopClick) {
                Icon(Icons.Default.Stop, contentDescription = null)
            }
            IconButton(onClick = onRecordCancelClick) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }
    }
}

@Composable
private fun ScanSpeakerRow(
    isPlaying: Boolean,
    isScanning: Boolean,
    isSpeakerOn: Boolean,
    onScanClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onScanClick, enabled = isPlaying && !isScanning) {
            if (isScanning) {
                CircularProgressIndicator(modifier = Modifier.padding(4.dp))
            } else {
                Icon(Icons.Default.Radar, contentDescription = null)
            }
        }

        IconButton(onClick = onSpeakerClick, enabled = isPlaying) {
            Icon(
                imageVector = if (isSpeakerOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun FrequencyTuningRow(
    currentFrequency: String,
    isPlaying: Boolean,
    onSeekDownClick: () -> Unit,
    onSeekUpClick: () -> Unit,
    onFrequencyConfirmed: (Double) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onSeekDownClick, enabled = isPlaying) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
        }

        FrequencyDisplay(
            currentFrequency = currentFrequency,
            onFrequencyConfirmed = onFrequencyConfirmed
        )

        IconButton(onClick = onSeekUpClick, enabled = isPlaying) {
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
        }
    }
}

@Composable
private fun RecordingStatusText(
    isRecordingPaused: Boolean,
    recordingTime: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = if (isRecordingPaused) "Запись приостановлена • $recordingTime" else "● Идёт запись... $recordingTime",
        color = if (isRecordingPaused) Color.Gray else Color.Red,
        modifier = modifier
    )
}