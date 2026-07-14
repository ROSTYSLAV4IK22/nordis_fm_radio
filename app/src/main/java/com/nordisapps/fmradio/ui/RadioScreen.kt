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
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    showScannedStations: Boolean,
    onScaleFrequencyChange: (Double) -> Unit,
    onSeekUpClick: () -> Unit,
    onSeekDownClick: () -> Unit,
    onPowerClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    onScanClick: () -> Unit,
    onConfirmScannedStations: () -> Unit,
    onSavedStationSelected: (Double) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            onClick = { onPowerClick() }
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onScanClick() },
                enabled = isPlaying && !isScanning
            ) {
                if (isScanning) {
                    CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                } else {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = null
                    )
                }
            }

            IconButton(
                onClick = { onSpeakerClick() },
                enabled = isPlaying
            ) {
                Icon(
                    imageVector = if (isSpeakerOn)
                        Icons.AutoMirrored.Filled.VolumeUp
                    else
                        Icons.AutoMirrored.Filled.VolumeOff,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 96.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onSeekDownClick,
                    enabled = isPlaying
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null
                    )
                }

                FrequencyDisplay(
                    currentFrequency = currentFrequency,
                    onFrequencyConfirmed = { newFreq ->
                        onScaleFrequencyChange(newFreq)
                    }
                )

                IconButton(
                    onClick = onSeekUpClick,
                    enabled = isPlaying
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            }

            Text(
                text = stationName
            )

            Text(
                text = radioText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FrequencyScale(
                currentFrequency = currentFrequency.toFloatOrNull() ?: 87.5f,
                isPlaying = isPlaying,
                onFrequencyChange = { newFreq ->
                    onScaleFrequencyChange(newFreq.toDouble())
                },
                modifier = Modifier.padding(top = 0.dp, bottom = 24.dp)
            )

            SavedStationsSection(
                stations = savedStations,
                onStationSelected = onSavedStationSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
    if (showScannedStations) {
        ScannedStationsSheet(
            stations = scannedStations,
            onConfirm = onConfirmScannedStations
        )
    }
}