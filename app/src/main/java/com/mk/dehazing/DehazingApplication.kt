package com.mk.dehazing

import android.app.Application
import com.mk.core.utilities.DCPUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DehazingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DCPUtils.initOpenCV()
    }
}