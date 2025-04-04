package dev.adryxta.octoview.data.model

import java.util.Date

sealed class User {
    abstract val id: Int
    abstract val login: String
    abstract val name: String?
    abstract val email: String?
    abstract val avatarUrl: String

    data class Profile(
        override val id: Int,
        override val login: String,
        override val name: String?,
        override val email: String?,
        override val avatarUrl: String
    ): User()

    data class Details(
        override val id: Int,
        override val login: String,
        override val name: String?,
        override val email: String?,
        override val avatarUrl: String,
        val company: String?,
        val blog: String?,
        val location: String?,
        val hireable: Boolean?,
        val bio: String?,
        val followers: Int,
        val following: Int,
        val publicRepos: Int,
        val publicGists: Int,
        val twitterUsername: String?,
        val createAt: Date,
        val updatedAt: Date
    ): User()
}