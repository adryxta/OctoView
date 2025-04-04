package dev.adryxta.octoview.data.mapper

import dev.adryxta.octoview.data.api.GitHubUserApi
import dev.adryxta.octoview.data.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val GITHUB_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

internal fun GitHubUserApi.User.toModel() = User.Profile(
    id = id,
    login = login,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
)

internal fun GitHubUserApi.UserDetails.toModel() = User.Details(
    id = id,
    login = login,
    name = name,
    email = email,
    avatarUrl = avatarUrl,
    company = company,
    blog = blog,
    location = location,
    hireable = hireable,
    bio = bio,
    followers = followers,
    following = following,
    publicRepos = publicRepos,
    publicGists = publicGists,
    twitterUsername = twitterUsername,
    createdAt = LocalDateTime.parse(createdAt, GITHUB_DATE_FORMAT),
    updatedAt = LocalDateTime.parse(updatedAt, GITHUB_DATE_FORMAT)
)
