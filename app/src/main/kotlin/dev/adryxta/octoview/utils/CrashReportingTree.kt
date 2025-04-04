package dev.adryxta.octoview.utils

import android.util.Log
import timber.log.Timber

class CrashReportingTree: Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when(priority) {
            Log.ASSERT, Log.ERROR -> {
                // Crashlytics fatal errors
            }
            Log.WARN -> {
                // Crashlytics non-fatal errors
            }
            else -> {
                // Ignore other log levels
            }
        }
    }
}