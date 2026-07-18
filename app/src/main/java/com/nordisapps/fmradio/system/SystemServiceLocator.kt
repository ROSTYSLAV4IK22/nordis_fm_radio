package com.nordisapps.fmradio.system

import android.annotation.SuppressLint
import android.os.IBinder

object SystemServiceLocator {
    @SuppressLint("PrivateApi")
    fun getService(name: String): IBinder {
        val serviceManagerClass = Class.forName("android.os.ServiceManager")
        val getServiceMethod = serviceManagerClass.getMethod("getService", String::class.java)
        return getServiceMethod.invoke(null, name) as IBinder
    }
}