package dev.adryxta.octoview.data

import dev.adryxta.octoview.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserList(previousUserId: Int? = null): List<User.Profile>

    fun getUserDetails(): Flow<User>
}

internal class DefaultUserRepository : UserRepository {
    override fun getUserList(previousUserId: Int?): List<User.Profile> {
        TODO("Not yet implemented")
    }

    override fun getUserDetails(): Flow<User> {
        TODO("Not yet implemented")
    }
}