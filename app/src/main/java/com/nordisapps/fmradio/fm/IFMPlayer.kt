package com.nordisapps.fmradio.fm

import android.os.IBinder
import android.os.Parcel

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

    class Proxy(
        private val remote: IBinder
    ) : IFMPlayer {
        companion object {

            const val DESCRIPTOR =
                "com.samsung.android.media.fmradio.internal.IFMPlayer"

            const val TRANSACTION_ON = 4
            const val TRANSACTION_IS_ON = 7
            const val TRANSACTION_OFF = 6
            const val TRANSACTION_TUNE = 3
            const val TRANSACTION_SEEK_DOWN = 9
            const val TRANSACTION_SEEK_UP = 8
            const val TRANSACTION_SCAN = 12
            const val TRANSACTION_CANCEL_SCAN = 13
            const val TRANSACTION_IS_SCANNING = 14
            const val TRANSACTION_GET_LAST_SCAN_RESULT = 33
            const val TRANSACTION_GET_CURRENT_CHANNEL = 11
            const val TRANSACTION_SET_SPEAKER = 40
            const val TRANSACTION_SET_STEREO = 34
            const val TRANSACTION_SET_SOFTMUTE = 47
            const val TRANSACTION_GET_SOFTMUTE = 48
            const val TRANSACTION_IS_HEADSET_PLUGGED = 38
            const val TRANSACTION_ENABLE_RDS = 20
            const val TRANSACTION_IS_RDS_ENABLE = 30
            const val TRANSACTION_ENABLE_DNS = 22
            const val TRANSACTION_IS_DNS_ENABLE = 24
            const val TRANSACTION_ENABLE_AF = 25
            const val TRANSACTION_DISABLE_AF = 26
            const val TRANSACTION_SET_LISTENER = 1
            const val TRANSACTION_GET_INTEGER_TUNNING_PARAMETER = 51
        }

        override fun on(): Boolean {

            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {

                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_ON,
                    data,
                    reply,
                    0
                )

                reply.readException()

                reply.readInt() != 0

            } finally {

                data.recycle()
                reply.recycle()
            }
        }

        override fun isOn(): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(TRANSACTION_IS_ON, data, reply, 0)

                reply.readException()
                reply.readInt() != 0
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun off(): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(TRANSACTION_OFF, data, reply, 0)

                reply.readException()
                reply.readInt() != 0
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun tune(freq: Long) {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {

                data.writeInterfaceToken(DESCRIPTOR)

                data.writeLong(freq)

                remote.transact(
                    TRANSACTION_TUNE,
                    data,
                    reply,
                    0
                )

                reply.readException()

            } finally {

                data.recycle()
                reply.recycle()
            }
        }

        override fun seekDown(): Long {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_SEEK_DOWN,
                    data,
                    reply,
                    0
                )

                reply.readException()
                reply.readLong()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun seekUp(): Long {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_SEEK_UP,
                    data,
                    reply,
                    0
                )

                reply.readException()
                reply.readLong()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun scan() {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(TRANSACTION_SCAN, data, reply, 0)

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun cancelScan(): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(TRANSACTION_CANCEL_SCAN, data, reply, 0)

                reply.readException()
                reply.readInt() != 0
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun isScanning(): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(TRANSACTION_IS_SCANNING, data, reply, 0)

                reply.readException()
                reply.readInt() != 0
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun getLastScanResult(): LongArray {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(TRANSACTION_GET_LAST_SCAN_RESULT, data, reply, 0)

                reply.readException()
                reply.createLongArray() ?: LongArray(0)
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun getCurrentChannel(): Long {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_GET_CURRENT_CHANNEL,
                    data,
                    reply,
                    0
                )

                reply.readException()
                reply.readLong()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun setSpeakerOn(enabled: Boolean) {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {

                data.writeInterfaceToken(DESCRIPTOR)

                data.writeInt(
                    if (enabled) 1 else 0
                )

                remote.transact(
                    TRANSACTION_SET_SPEAKER,
                    data,
                    reply,
                    0
                )

                reply.readException()

            } finally {

                data.recycle()
                reply.recycle()
            }
        }

        override fun setStereo() {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_SET_STEREO,
                    data,
                    reply,
                    0
                )

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun setSoftmute(enabled: Boolean) {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {

                data.writeInterfaceToken(DESCRIPTOR)

                data.writeInt(
                    if (enabled) 1 else 0
                )

                remote.transact(
                    TRANSACTION_SET_SOFTMUTE,
                    data,
                    reply,
                    0
                )

                reply.readException()

            } finally {

                data.recycle()
                reply.recycle()
            }
        }

        override fun getSoftMuteMode(): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_GET_SOFTMUTE,
                    data,
                    reply,
                    0
                )

                reply.readException()

                return reply.readInt() != 0
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun isHeadsetPlugged(): Boolean {

            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {

                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_IS_HEADSET_PLUGGED,
                    data,
                    reply,
                    0
                )

                reply.readException()

                reply.readInt() != 0

            } finally {

                data.recycle()
                reply.recycle()
            }
        }

        override fun enableRDS() {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_ENABLE_RDS,
                    data,
                    reply,
                    0
                )

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun isRDSEnable(): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_IS_RDS_ENABLE,
                    data,
                    reply,
                    0
                )

                reply.readException()
                reply.readInt() != 0
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun enableDNS() {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_ENABLE_DNS,
                    data,
                    reply,
                    0
                )

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun isDNSEnable(): Boolean {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_IS_DNS_ENABLE,
                    data,
                    reply,
                    0
                )

                reply.readException()
                reply.readInt() != 0
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun enableAF() {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_ENABLE_AF,
                    data,
                    reply,
                    0
                )

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun disableAF() {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                remote.transact(
                    TRANSACTION_DISABLE_AF,
                    data,
                    reply,
                    0
                )

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun setListener(listener: IFMEventListener) {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)

                data.writeStrongBinder(listener.asBinder())

                remote.transact(
                    TRANSACTION_SET_LISTENER,
                    data,
                    reply,
                    0
                )

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun getIntegerTunningParameter(
            key: String,
            defaultValue: Int
        ): Int {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            return try {
                data.writeInterfaceToken(DESCRIPTOR)
                data.writeString(key)
                data.writeInt(defaultValue)

                remote.transact(
                    TRANSACTION_GET_INTEGER_TUNNING_PARAMETER,
                    data,
                    reply,
                    0
                )

                reply.readException()
                reply.readInt()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }
    }
}