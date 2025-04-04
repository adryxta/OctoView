package dev.adryxta.octoview.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.adryxta.octoview.data.api.GitHubUserApi
import okhttp3.Cache
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module that provides dependencies for the data layer.
 * Need dependent [String] values to create the Rest Client.
 * @see DataModule.API_BASE_URL
 * @see DataModule.API_AUTH_TOKEN
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    internal fun provideApiService(
        @Named(API_BASE_URL) baseUrl: String,
        @Named(API_AUTH_TOKEN) authToken: String,
        cache: Cache?
    ): GitHubUserApi {
        return GitHubUserApi.create(baseUrl, authToken, cache)
    }

    @Provides
    @Singleton
    internal fun provideRetrofitCache(@ApplicationContext appContext: Context): Cache? {
        return runCatching {
            val cacheFile = File(appContext.cacheDir, "${appContext.packageName}.cache")
            if (!cacheFile.exists()) {
                cacheFile.mkdir()
            }
            Cache(cacheFile, DISK_CACHE_SIZE)
        }.getOrNull()
    }

    @Provides
    @Singleton
    internal fun provideUserRepository(
        gitHubUserApi: GitHubUserApi
    ): UserRepository {
        return DefaultUserRepository(gitHubUserApi)
    }

    companion object {
        const val API_BASE_URL = "api-base-url"
        const val API_AUTH_TOKEN = "api-auth-token"
        const val DISK_CACHE_SIZE = 5 * 1024 * 1024L
    }
}