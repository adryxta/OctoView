package dev.adryxta.octoview.data.api

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import okhttp3.Interceptor as OkHttpInterceptor

class TimberLogger: HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Timber.tag("OkHttp").v(message)
    }

    companion object {
        val Interceptor: OkHttpInterceptor = HttpLoggingInterceptor(TimberLogger()).apply {
            redactHeader("Authorization")
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
    }
}