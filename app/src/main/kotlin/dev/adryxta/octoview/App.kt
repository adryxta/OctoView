package dev.adryxta.octoview

import android.app.Application
import dev.adryxta.octoview.utils.CrashReportingTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber initialized.")
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}