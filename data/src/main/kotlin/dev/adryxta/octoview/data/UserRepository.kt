package dev.adryxta.octoview.data

import dev.adryxta.octoview.data.api.GitHubUserApi
import dev.adryxta.octoview.data.mapper.toModel
import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

typealias UserLogin = String

interface UserRepository {

    suspend fun getUserList(previousUserId: Int? = null): Result<List<User.Profile>>

    fun getUserDetails(login: UserLogin): Flow<User>
}

internal class DefaultUserRepository @Inject constructor(
    private val gitHubUserApi: GitHubUserApi,
) : UserRepository {

    private val userProfileCache = mutableMapOf<UserLogin, User.Profile>()

    override suspend fun getUserList(previousUserId: Int?): Result<List<User.Profile>> {
        try {
            val users = gitHubUserApi.getUsers(previousUserId)
            val profileModels = users.map { it.toModel() }
            userProfileCache.putAll(profileModels.associateBy { it.login })
            return Result.success(profileModels).also {
                Timber.d("Returned user list: $profileModels")
            }
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Failed to get user list")
            return Result.failure(throwable)
        }
    }

    override fun getUserDetails(login: UserLogin): Flow<User> = flow {
        val cachedUserProfile = userProfileCache.getOrDefault(login, null)
        if (cachedUserProfile != null) {
            Timber.d("Emitted cached user profile for $login")
            emit(cachedUserProfile)
        } else {
            Timber.w("User profile for $login is not cached")
        }

        try {
            val userDetails = gitHubUserApi.getUserDetail(login)
            emit(userDetails.toModel())
            Timber.d("Emitted user details for $login: $userDetails")
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Failed to get user details for $login")
            throw throwable
        }
    }
}