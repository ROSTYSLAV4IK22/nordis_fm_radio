package com.nordisapps.fmradio.fm

import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel

interface IFMEventListener : IInterface {
    fun onRadioEnabled()

    fun onRadioDataSystemReceived(
        freq: Long,
        channelName: String?,
        radioText: String?
    )

    fun onScanStarted()

    fun onChannelFound(freq: Long)

    fun onScanFinished(freqs: LongArray)

    fun onScanStopped(freqs: LongArray)

    fun onTuned(freq: Long)

    abstract class Stub :
        Binder(),
        IFMEventListener {

        companion object {
            const val DESCRIPTOR =
                "com.samsung.android.media.fmradio.internal.IFMEventListener"
            const val TRANSACTION_ON_RADIO_ENABLED = 1
            const val TRANSACTION_ON_CHANNEL_FOUND = 3
            const val TRANSACTION_ON_SCAN_STARTED = 4
            const val TRANSACTION_ON_SCAN_STOPPED = 5
            const val TRANSACTION_ON_SCAN_FINISHED = 6
            const val TRANSACTION_ON_TUNED = 7
            const val TRANSACTION_ON_RDS_RECEIVED = 10
        }

        init {
            attachInterface(this, DESCRIPTOR)
        }

        override fun asBinder(): IBinder {
            return this
        }

        override fun onTransact(
            code: Int,
            data: Parcel,
            reply: Parcel?,
            flags: Int
        ): Boolean {
            when (code) {
                TRANSACTION_ON_RADIO_ENABLED -> {
                    data.enforceInterface(DESCRIPTOR)
                    onRadioEnabled()
                    return true
                }

                TRANSACTION_ON_CHANNEL_FOUND -> {
                    data.enforceInterface(DESCRIPTOR)
                    val freq = data.readLong()
                    onChannelFound(freq)
                    return true
                }

                TRANSACTION_ON_SCAN_STARTED -> {
                    data.enforceInterface(DESCRIPTOR)
                    onScanStarted()
                    return true
                }

                TRANSACTION_ON_SCAN_STOPPED -> {
                    data.enforceInterface(DESCRIPTOR)
                    val size = data.readInt()
                    val freqs = if (size < 0) LongArray(0) else LongArray(size)
                    onScanStopped(freqs)
                    reply?.writeNoException()
                    reply?.writeLongArray(freqs)
                    return true
                }

                TRANSACTION_ON_SCAN_FINISHED -> {
                    data.enforceInterface(DESCRIPTOR)
                    val size = data.readInt()
                    val freqs = if (size < 0) LongArray(0) else LongArray(size)
                    onScanFinished(freqs)
                    reply?.writeNoException()
                    reply?.writeLongArray(freqs)
                    return true
                }

                TRANSACTION_ON_TUNED -> {
                    data.enforceInterface(DESCRIPTOR)
                    val freq = data.readLong()
                    onTuned(freq)
                    return true
                }

                TRANSACTION_ON_RDS_RECEIVED -> {
                    data.enforceInterface(DESCRIPTOR)
                    val freq = data.readLong()
                    val channelName =
                        data.readString()
                    val radioText =
                        data.readString()

                    onRadioDataSystemReceived(
                        freq,
                        channelName,
                        radioText
                    )
                    return true
                }

                INTERFACE_TRANSACTION -> {
                    reply?.writeString(DESCRIPTOR)
                    return true
                }
            }
            return super.onTransact(code, data, reply, flags)
        }
    }
}