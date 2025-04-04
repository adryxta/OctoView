package dev.adryxta.octoview.data.mapper

import android.icu.text.DateFormat
import dev.adryxta.octoview.data.api.GitHubUserApi
import dev.adryxta.octoview.data.model.User

internal val GITHUB_DATE_FORMAT = DateFormat.getPatternInstance("yyyy-MM-dd'T'HH:mm:ss'Z'")


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
    createAt = GITHUB_DATE_FORMAT.parse(createdAt),
    updatedAt = GITHUB_DATE_FORMAT.parse(updatedAt)
)

