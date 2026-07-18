package com.nordisapps.fmradio.system

import android.media.AudioManager

object SamsungAudioRouter {
    private const val PATH_SPEAKER = 2
    private const val PATH_HEADSET = 3

    fun setRadioOutputPath(audioManager: AudioManager, toSpeaker: Boolean): Boolean {
        val audioManagerClass = AudioManager::class.java

        val setMethod = audioManagerClass.getMethod(
            "semSetRadioOutputPath", Int::class.javaPrimitiveType
        )
        val pathValue = if (toSpeaker) PATH_SPEAKER else PATH_HEADSET
        setMethod.invoke(audioManager, pathValue)

        val getMethod = audioManagerClass.getMethod("semGetRadioOutputPath")
        val currentPath = getMethod.invoke(audioManager) as Int

        return currentPath == PATH_SPEAKER
    }
}