package dev.adryxta.octoview.data

import dev.adryxta.octoview.data.api.GitHubUserApi
import dev.adryxta.octoview.data.mapper.toModel
import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

typealias UserLogin = String

interface UserRepository {

    suspend fun getUserList(previousUserId: Int? = null): Result<List<User.Profile>>

    fun getUserDetails(login: UserLogin): Flow<User>
}

internal class DefaultUserRepository(
    private val gitHubUserApi: GitHubUserApi
) : UserRepository {

    private val userProfileCache = mutableMapOf<UserLogin, User.Profile>()

    override suspend fun getUserList(previousUserId: Int?): Result<List<User.Profile>> {
        try {
            val users = gitHubUserApi.getUsers(previousUserId)
            val profileModels = users.map { it.toModel() }
            userProfileCache.putAll(profileModels.map { it.login to it })
            return Result.success(profileModels)
        } catch (throwable: Throwable) {
            return Result.failure(throwable)
        }
    }

    override fun getUserDetails(login: UserLogin): Flow<User> = flow {
        val cachedUserProfile = userProfileCache.getOrDefault(login, null)
        if (cachedUserProfile != null) {
            emit(cachedUserProfile)
        }

        try {
            val userDetails = gitHubUserApi.getUserDetail(login)
            emit(userDetails.toModel())
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}