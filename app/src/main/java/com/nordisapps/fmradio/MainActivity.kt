package com.nordisapps.fmradio

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.nordisapps.fmradio.ui.AppNavHost
import com.nordisapps.fmradio.ui.RadioPlayerScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<RadioViewModel>()
    private val stationsDataStore by lazy { StationsDataStore(this) }
    private val favoriteStationsDataStore by lazy { FavoriteStationsDataStore(this) }
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}
    private val radioManager by lazy {
        FmRadioManager(this)
    }

    private fun tuneToStation(freq: Double) {
        lifecycleScope.launch {
            val tuned = withContext(Dispatchers.IO) { radioManager.tuneSafe(freq) }
            viewModel.updateCurrentFrequency(tuned.toString())

            if (viewModel.uiState.isPlaying) {
                val serviceIntent = Intent(this@MainActivity, FmRadioForegroundService::class.java).apply {
                    putExtra(FmRadioForegroundService.EXTRA_FREQUENCY, tuned.toString())
                }
                startForegroundService(serviceIntent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        radioManager.onStationNameReceived = { stationName ->
            viewModel.updateStationName(stationName)
        }

        radioManager.onRdsCleared = {
            viewModel.clearRds()
        }

        radioManager.onRadioTextReceived = { radioText ->
            viewModel.updateRadioText(radioText)
        }

        radioManager.onScanStarted = {
            viewModel.updateScanning(true)
            viewModel.updateScannedStations(emptyList())
        }

        radioManager.onChannelFound = { freq ->
            viewModel.updateScannedStations(viewModel.uiState.scannedStations + freq)
        }

        radioManager.onScanFinished = {
            viewModel.updateScanning(false)
            lifecycleScope.launch {
                val stations = withContext(Dispatchers.IO) {
                    radioManager.getLastScanResult()
                }
                Log.d("FMTEST", "getLastScanResult() = $stations")
                if (stations.isNotEmpty()) {
                    viewModel.updateScannedStations(stations)
                }
                viewModel.updateShowScannedStations(true)
            }
        }

        radioManager.onScanStopped = { stations ->
            viewModel.updateScanning(false)
            viewModel.updateScannedStations(stations)
        }

        lifecycleScope.launch {
            val saved = stationsDataStore.getSavedStations()
            viewModel.updateSavedStations(saved)

            val favorites = favoriteStationsDataStore.getFavoriteStations()
            viewModel.setFavoriteStations(favorites)
        }

        setContent {
            AppNavHost(
                favoriteStations = viewModel.uiState.favoriteStations.sorted(),
                onStationSelected = { freq ->
                    tuneToStation(freq)
                },
                onFavoriteToggle = { freq ->
                    viewModel.toggleFavorite(freq)
                    lifecycleScope.launch {
                        favoriteStationsDataStore.saveFavoriteStations(viewModel.uiState.favoriteStations)
                    }
                },
                homeContent = {
                    RadioPlayerScreen(
                        currentFrequency = viewModel.uiState.currentFrequency,
                        stationName = viewModel.uiState.stationName,
                        radioText = viewModel.uiState.radioText,
                        isPlaying = viewModel.uiState.isPlaying,
                        isScanning = viewModel.uiState.isScanning,
                        isSpeakerOn = viewModel.uiState.isSpeakerOn,
                        scannedStations = viewModel.uiState.scannedStations,
                        savedStations = viewModel.uiState.savedStations,
                        favoriteStations = viewModel.uiState.favoriteStations,
                        showScannedStations = viewModel.uiState.showScannedStations,
                        onSeekDownClick = {
                            lifecycleScope.launch {
                                val frequency = withContext(Dispatchers.IO) {
                                    radioManager.seekDown()
                                }

                                viewModel.updateCurrentFrequency(
                                    frequency.toString()
                                )

                                if (viewModel.uiState.isPlaying) {
                                    val serviceIntent = Intent(this@MainActivity, FmRadioForegroundService::class.java).apply {
                                        putExtra(FmRadioForegroundService.EXTRA_FREQUENCY, frequency.toString())
                                    }
                                    startForegroundService(serviceIntent)
                                }
                            }
                        },
                        onSeekUpClick = {
                            lifecycleScope.launch {
                                val frequency = withContext(Dispatchers.IO) {
                                    radioManager.seekUp()
                                }

                                viewModel.updateCurrentFrequency(
                                    frequency.toString()
                                )

                                if (viewModel.uiState.isPlaying) {
                                    val serviceIntent = Intent(this@MainActivity, FmRadioForegroundService::class.java).apply {
                                        putExtra(FmRadioForegroundService.EXTRA_FREQUENCY, frequency.toString())
                                    }
                                    startForegroundService(serviceIntent)
                                }
                            }
                        },
                        onPowerClick = {
                            lifecycleScope.launch {
                                if (viewModel.uiState.isPlaying) {
                                    withContext(Dispatchers.IO) {
                                        radioManager.stop()
                                    }
                                    viewModel.updatePlaying(false)
                                    viewModel.clearRds()
                                    stopService(Intent(this@MainActivity, FmRadioForegroundService::class.java))
                                } else {
                                    val frequency = viewModel.uiState.currentFrequency
                                        .toDoubleOrNull()
                                        ?: 87.5

                                    val tunedFrequency = withContext(Dispatchers.IO) {
                                        radioManager.play()
                                        radioManager.tuneSafe(frequency)
                                    }

                                    viewModel.updateCurrentFrequency(
                                        tunedFrequency.toString()
                                    )
                                    viewModel.updatePlaying(true)

                                    val serviceIntent = Intent(this@MainActivity, FmRadioForegroundService::class.java).apply {
                                        putExtra(FmRadioForegroundService.EXTRA_FREQUENCY, tunedFrequency.toString())
                                    }
                                    startForegroundService(serviceIntent)
                                }
                            }
                        },
                        onSpeakerClick = {
                            lifecycleScope.launch {
                                val newSpeakerState = !viewModel.uiState.isSpeakerOn
                                val result = withContext(Dispatchers.IO) {
                                    radioManager.setSpeakerOn(newSpeakerState)
                                }
                                viewModel.updateSpeakerOn(result)
                            }
                        },
                        onScaleFrequencyChange = { newFreq ->
                            lifecycleScope.launch {
                                val tunedFrequency = withContext(Dispatchers.IO) {
                                    radioManager.tuneSafe(newFreq)
                                }
                                viewModel.updateCurrentFrequency(tunedFrequency.toString())

                                if (viewModel.uiState.isPlaying) {
                                    val serviceIntent = Intent(this@MainActivity, FmRadioForegroundService::class.java).apply {
                                        putExtra(FmRadioForegroundService.EXTRA_FREQUENCY, tunedFrequency.toString())
                                    }
                                    startForegroundService(serviceIntent)
                                }
                            }
                        },
                        onScanClick = {
                            lifecycleScope.launch {
                                withContext(Dispatchers.IO) {
                                    radioManager.scan()
                                }
                            }
                        },
                        onConfirmScannedStations = {
                            val merged = (viewModel.uiState.savedStations + viewModel.uiState.scannedStations)
                                .distinct()
                                .sorted()
                            viewModel.updateSavedStations(merged)
                            viewModel.updateShowScannedStations(false)
                            lifecycleScope.launch {
                                stationsDataStore.saveStations(merged)
                            }
                        },
                        onSavedStationSelected = { freq ->
                            tuneToStation(freq)
                        },
                        onFavoriteToggle = { freq ->
                            viewModel.toggleFavorite(freq)
                            lifecycleScope.launch {
                                favoriteStationsDataStore.saveFavoriteStations(viewModel.uiState.favoriteStations)
                            }
                        }
                    )
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            radioManager.onStationNameReceived = null
            radioManager.onRadioTextReceived = null
            radioManager.onRdsCleared = null
            radioManager.stop()
            Log.d("FMTEST", "RADIO OFF ON DESTROY")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}