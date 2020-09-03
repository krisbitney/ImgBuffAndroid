package com.imgbuff.colorizer

import android.app.Application
import timber.log.Timber

class ImgBuffApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}