package dev.adryxta.octoview

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import dev.adryxta.octoview.data.DataModule
import dev.adryxta.octoview.utils.CrashReportingTree
import timber.log.Timber
import javax.inject.Named

@HiltAndroidApp
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

    @Module
    @InstallIn(SingletonComponent::class)
    class AppModule {

        @Provides
        @Named(DataModule.API_BASE_URL)
        internal fun provideApiBaseUrl(): String {
            return BuildConfig.API_BASE_URL
        }

        @Provides
        @Named(DataModule.API_AUTH_TOKEN)
        internal fun provideAuthToken(): String {
            return BuildConfig.API_AUTH_TOKEN
        }
    }
}