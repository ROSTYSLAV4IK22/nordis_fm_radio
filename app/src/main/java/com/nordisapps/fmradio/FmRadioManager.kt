package com.nordisapps.fmradio

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.os.IBinder
import android.util.Log
import com.nordisapps.fmradio.fm.IFMPlayer
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.getValue
import kotlin.math.roundToInt

class FmRadioManager(context: Context) {
    private val radio by lazy { connectRadio() }
    private val audioManager = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var lastTuneTimestamp = 0L
    private val minTuneIntervalMs = 1500L
    private val tuneMutex = Mutex()
    var onStationNameReceived: ((String) -> Unit)? = null
    var onRadioTextReceived: ((String) -> Unit)? = null
    var onScanStarted: (() -> Unit)? = null
    var onChannelFound: ((Double) -> Unit)? = null
    var onScanFinished: (() -> Unit)? = null
    var onScanStopped: ((List<Double>) -> Unit)? = null
    private val listener = object : FMEventListener() {
        override fun onRadioDataSystemReceived(
            freq: Long,
            channelName: String?,
            radioText: String?
        ) {
            if (!channelName.isNullOrBlank()) {
                onStationNameReceived?.invoke(channelName)
            }
            if (!radioText.isNullOrBlank()) {
                onRadioTextReceived?.invoke(radioText)
            }

            Log.d("FMTEST", "RDS = $channelName | $radioText")
        }

        override fun onScanStarted() {
            Log.d("FMTEST", "SCAN STARTED")
            this@FmRadioManager.onScanStarted?.invoke()
        }

        override fun onChannelFound(freq: Long) {
            Log.d("FMTEST", "CHANNEL FOUND: $freq")
            this@FmRadioManager.onChannelFound?.invoke(freq / 1000.0)
        }

        override fun onScanFinished(freqs: LongArray) {
            Log.d("FMTEST", "SCAN FINISHED (event only, ignoring empty array)")
            this@FmRadioManager.onScanFinished?.invoke()
        }

        override fun onScanStopped(freqs: LongArray) {
            Log.d("FMTEST", "SCAN STOPPED: ${freqs.toList()}")
            this@FmRadioManager.onScanStopped?.invoke(freqs.map { it / 1000.0 })
        }
    }

    @SuppressLint("PrivateApi")
    private fun connectRadio(): IFMPlayer.Proxy {
        val serviceManagerClass =
            Class.forName("android.os.ServiceManager")

        val getServiceMethod =
            serviceManagerClass.getMethod(
                "getService",
                String::class.java
            )

        val binder =
            getServiceMethod.invoke(
                null,
                "FMPlayer"
            ) as IBinder

        Log.d(
            "FMTEST",
            "BINDER = $binder"
        )

        return IFMPlayer.Proxy(binder)
    }

    fun play() {
        try {
            val headset = radio.isHeadsetPlugged()
            Log.d("FMTEST", "HEADSET = $headset")
            if (!headset) {
                return
            }

            val on = radio.on()
            Log.d("FMTEST", "RADIO ON = $on")

            radio.setListener(listener)
            radio.enableRDS()
            radio.disableAF()
            radio.setStereo()
            radio.setSoftmute(false)
            Log.d("FMTEST", "SoftMute = ${radio.getSoftMuteMode()}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun tune(frequency: Double): Double {
        if (!radio.isOn()) {
            return (frequency * 20).roundToInt() / 20.0
        }

        val now = System.currentTimeMillis()
        val elapsed = now - lastTuneTimestamp
        if (elapsed < minTuneIntervalMs) {
            Thread.sleep(minTuneIntervalMs - elapsed)
        }
        lastTuneTimestamp = System.currentTimeMillis()

        return try {
            radio.tune((frequency * 1000).toLong())
            Log.d("FMTEST", "TUNED TO $frequency")

            val currentChannel = radio.getCurrentChannel()
            Log.d("FMTEST", "CURRENT CHANNEL: $currentChannel")
            val result = currentChannel / 1000.0
            (result * 20).roundToInt() / 20.0
        } catch (e: Exception) {
            e.printStackTrace()
            frequency
        }
    }

    suspend fun tuneSafe(frequency: Double): Double {
        return tuneMutex.withLock {
            tune(frequency)
        }
    }

    fun seekUp(): Double {
        if (!radio.isOn()) {
            return 87.5
        }

        return try {
            val frequency = radio.seekUp()

            Log.d("FMTEST", "SEEK UP = $frequency")

            frequency / 1000.0
        } catch (e: Exception) {
            e.printStackTrace()
            87.5
        }
    }

    fun seekDown(): Double {
        if (!radio.isOn()) {
            return 87.5
        }

        return try {
            val frequency = radio.seekDown()

            Log.d("FMTEST", "SEEK DOWN = $frequency")

            frequency / 1000.0
        } catch (e: Exception) {
            e.printStackTrace()
            87.5
        }
    }

    fun scan() {
        try {
            radio.scan()
            Log.d("FMTEST", "SCAN CALLED")
        } catch (e: Exception) {
            Log.e("FMTEST", "scan EXCEPTION: ${e.message}", e)
        }
    }

    fun cancelScan(): Boolean {
        return try {
            radio.cancelScan()
        } catch (e: Exception) {
            Log.e("FMTEST", "cancelScan EXCEPTION: ${e.message}", e)
            false
        }
    }

    fun isScanning(): Boolean {
        return try {
            radio.isScanning()
        } catch (e: Exception) {
            false
        }
    }

    fun getLastScanResult(): List<Double> {
        return try {
            radio.getLastScanResult().map { it / 1000.0 }
        } catch (e: Exception) {
            Log.e("FMTEST", "getLastScanResult EXCEPTION: ${e.message}", e)
            emptyList()
        }
    }

    fun setSpeakerOn(enabled: Boolean): Boolean {
        return try {
            radio.setSpeakerOn(enabled)

            val audioManagerClass = AudioManager::class.java

            val semSetRadioOutputPathMethod = audioManagerClass.getMethod(
                "semSetRadioOutputPath",
                Int::class.javaPrimitiveType
            )

            val pathValue = if (enabled) 2 else 3
            val setResult = semSetRadioOutputPathMethod.invoke(audioManager, pathValue)
            Log.d("FMTEST", "semSetRadioOutputPath($pathValue) result = $setResult")

            val semGetRadioOutputPathMethod = audioManagerClass.getMethod("semGetRadioOutputPath")
            val currentPath = semGetRadioOutputPathMethod.invoke(audioManager) as Int
            Log.d("FMTEST", "semGetRadioOutputPath() = $currentPath")

            currentPath == 2
        } catch (e: Exception) {
            Log.e("FMTEST", "setSpeakerOn EXCEPTION: ${e.message}", e)
            false
        }
    }

    fun getSoftMuteMode(): Boolean {
        if (!radio.isOn()) {
            return false
        }

        return try {
            radio.getSoftMuteMode()
        } catch (e: Exception) {
            Log.e("FMTEST", "EXCEPTION: ${e.message}", e)
            false
        }
    }

    fun getIntegerTunningParameter(
        key: String,
        defaultValue: Int
    ): Int {
        return try {
            radio.getIntegerTunningParameter(key, defaultValue)
        } catch (e: Exception) {
            Log.e("FMTEST", "EXCEPTION: ${e.message}", e)
            defaultValue
        }
    }

    fun stop() {
        try {
            val off = radio.off()
            Log.d("FMTEST", "RADIO OFF = $off")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}