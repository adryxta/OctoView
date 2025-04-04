package dev.adryxta.octoview.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.adryxta.octoview.data.api.GitHubUserApi
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
        @Named(API_AUTH_TOKEN) authToken: String
    ): GitHubUserApi {
        return GitHubUserApi.create(baseUrl, authToken)
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
    }
}