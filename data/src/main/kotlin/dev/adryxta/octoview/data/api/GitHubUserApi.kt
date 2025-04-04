package dev.adryxta.octoview.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

internal interface GitHubUserApi {

    @GET("/users")
    fun getUsers(
        @Query("since") lastUserId: Int? = null,
        @Query("per_page") perPage: Int = 30
    ): Call<List<User>>

    @GET("/users/{login}")
    fun getUserDetail(@Query("login") login: String): Call<UserDetails>

    @Serializable
    data class User(
        val id: Int,
        val login: String,
        val name: String?,
        val email: String?,
        val type: String,
        // https://api.github.com/users/octocat
        val url: String,
        @SerialName("avatar_url")
        val avatarUrl: String,
        @SerialName("gravatar_id")
        val gravatarId: String?,
        @SerialName("html_url")
        val htmlUrl: String,
        @SerialName("followers_url")
        val followersUrl: String,
        @SerialName("following_url")
        val followingUrl: String,
        @SerialName("gists_url")
        val gistsUrl: String,
        @SerialName("starred_url")
        val starredUrl: String,
        @SerialName("subscriptions_url")
        val subscriptionsUrl: String,
        @SerialName("organizations_url")
        val organizationsUrl: String,
        @SerialName("repos_url")
        val reposUrl: String,
        @SerialName("events_url")
        val eventsUrl: String,
        @SerialName("received_events_url")
        val receivedEventsUrl: String,
    )

    @Serializable
    data class UserDetails(
        val id: Int,
        val login: String,
        val name: String?,
        val email: String?,
        val type: String,
        @SerialName("avatar_url")
        val avatarUrl: String,
        @SerialName("gravatar_id")
        val gravatarId: String,
        val url: String,
        val company: String?,
        val blog: String?,
        val location: String?,
        val hireable: Boolean?,
        val bio: String?,
        val followers: Int,
        val following: Int,
        @SerialName("public_repos")
        val publicRepos: Int,
        @SerialName("public_gists")
        val publicGists: Int,
        @SerialName("twitter_username")
        val twitterUsername: String?,
        @SerialName("html_url")
        val htmlUrl: String,
        @SerialName("followers_url")
        val followersUrl: String,
        @SerialName("following_url")
        val followingUrl: String,
        @SerialName("gists_url")
        val gistsUrl: String,
        @SerialName("starred_url")
        val starredUrl: String,
        @SerialName("subscriptions_url")
        val subscriptionsUrl: String,
        @SerialName("organizations_url")
        val organizationsUrl: String,
        @SerialName("repos_url")
        val reposUrl: String,
        @SerialName("events_url")
        val eventsUrl: String,
        @SerialName("received_events_url")
        val receivedEventsUrl: String,
        @SerialName("created_at")
        val createdAt: String,
        @SerialName("updated_at")
        val updatedAt: String
    )

    companion object {
        fun create(baseUrl: String): GitHubUserApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(
                    Json.asConverterFactory(
                        MediaType.get("application/json; charset=UTF8")
                    )
                ).build()
            return retrofit.create(GitHubUserApi::class.java)
        }
    }
}