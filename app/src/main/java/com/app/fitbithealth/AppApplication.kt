package com.app.fitbithealth

import android.app.Application
import com.app.fitbithealth.injection.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * App Application class to initialize the KOIN and Timber
 * Timber library used for printing logs
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AppApplication)
            modules(appModules)
        }
    }
}