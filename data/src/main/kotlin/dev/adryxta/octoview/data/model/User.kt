package dev.adryxta.octoview.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
sealed class User {
    abstract val id: Int
    abstract val login: String
    abstract val name: String?
    abstract val email: String?
    abstract val avatarUrl: String

    @Serializable
    data class Profile(
        override val id: Int,
        override val login: String,
        override val name: String?,
        override val email: String?,
        override val avatarUrl: String
    ) : User()

    @Serializable
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
        @Contextual
        val createdAt: LocalDateTime,
        @Contextual
        val updatedAt: LocalDateTime
    ) : User()
}