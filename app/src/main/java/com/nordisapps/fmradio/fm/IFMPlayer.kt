package com.nordisapps.fmradio.fm

interface IFMPlayer {
    fun on(): Boolean

    fun isOn(): Boolean

    fun off(): Boolean

    fun tune(freq: Long)

    fun seekDown(): Long

    fun seekUp(): Long

    fun scan()

    fun cancelScan(): Boolean

    fun isScanning(): Boolean

    fun getLastScanResult(): LongArray

    fun getCurrentChannel(): Long

    fun setSpeakerOn(enabled: Boolean)

    fun setStereo()

    fun setSoftmute(enabled: Boolean)

    fun getSoftMuteMode(): Boolean

    fun isHeadsetPlugged(): Boolean

    fun enableRDS()

    fun enableDNS()

    fun isDNSEnable(): Boolean

    fun isRDSEnable(): Boolean

    fun enableAF()

    fun disableAF()

    fun setListener(listener: IFMEventListener)

    fun getIntegerTunningParameter(
        key: String,
        defaultValue: Int
    ): Int
}