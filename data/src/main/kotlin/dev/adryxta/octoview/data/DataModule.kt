package dev.adryxta.octoview.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import dev.adryxta.octoview.data.api.GitHubUserApi
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    internal fun provideApiService(
        @Named(DataModule.API_BASE_URL) baseUrl: String,
        @Named(DataModule.AUTH_TOKEN) authToken: String
    ): GitHubUserApi {
        return GitHubUserApi.create(baseUrl, authToken)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindUserRepository(
        userRepositoryImpl: DefaultUserRepository
    ): UserRepository

    companion object {
        const val API_BASE_URL = "api-base-url"
        const val AUTH_TOKEN = "auth-token"
    }
}