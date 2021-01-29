package com.app.fitbithealth

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * App Application class to initialize the KOIN and Timber
 * Timber library used for printing logs
 */
@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}