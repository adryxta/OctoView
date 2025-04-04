package dev.adryxta.octoview.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.theme.Avocado
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun UserDetail(
    /**
     * [profile] is available immediately when the user is clicked.
     */
    profile: User.Profile,
    /**
     * [details] is available after the user profile is fetched (network delay).
     */
    details: User.Details? = null,
    onClickBlog: (blog: String) -> Unit,
    onClickMail: (email: String) -> Unit,
    onClickX: (username: String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 360.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        AsyncImage(
                            model = profile.avatarUrl,
                            contentDescription = stringResource(R.string.cd_avatar),
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(164.dp)
                        )
                    }
                    if (details != null) {
                        UserDetailTopGridComponent(details)
                    }
                }
                details?.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(16.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        item {
            Column {
                details?.bio?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 5.dp),
                    )
                }
                details?.company?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.office),
                            contentDescription = stringResource(R.string.cd_ic_office),
                        )
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 5.dp),
                            maxLines = 1,
                        )
                    }
                }
                details?.location?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location),
                            contentDescription = stringResource(R.string.cd_ic_location),
                        )
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 5.dp),
                            maxLines = 1,
                        )
                    }
                }
                details?.blog?.takeIf { it.isNotEmpty() }
                    ?.let { //removes both null and empty blogs value
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 5.dp)
                                .clickable {
                                    onClickBlog(it)
                                }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.link),
                                contentDescription = stringResource(R.string.cd_ic_link),
                            )
                            Text(
                                text = it,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 5.dp),
                                maxLines = 1,
                            )
                        }
                    }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                ) {
                    details?.email?.let {
                        IconButton(
                            colors = IconButtonDefaults.filledTonalIconButtonColors(),
                            onClick = { onClickMail.invoke(it) },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_mail),
                                contentDescription = stringResource(R.string.cd_ic_mail),
                            )
                        }
                    }
                    details?.twitterUsername?.let {
                        IconButton(
                            colors = IconButtonDefaults.filledTonalIconButtonColors(),
                            onClick = { onClickX.invoke(it) },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_twitter_x),
                                contentDescription = stringResource(R.string.cd_ic_x),
                            )
                        }
                    }
                    details?.hireable?.let {
                        Icon(
                            painter = painterResource(R.drawable.online),
                            contentDescription = stringResource(R.string.cd_ic_hire),
                            modifier = Modifier.padding(start = 16.dp, end = 5.dp),
                            tint = Avocado
                        )
                        Text(
                            text = stringResource(R.string.hireable_text),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserDetailTopGridComponent(
    details: User.Details
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row {
            GridSegment(
                counter = formatFollowersCount(details.followers),
                label = stringResource(R.string.user_followers),
            )
            GridSegment(
                counter = formatFollowersCount(details.following),
                label = stringResource(R.string.user_following),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            GridSegment(
                counter = formatFollowersCount(details.publicRepos),
                label = stringResource(R.string.user_repos)
            )
            GridSegment(
                counter = formatFollowersCount(details.publicGists),
                label = stringResource(R.string.user_gists)
            )
        }

    }
}

@Composable
fun RowScope.GridSegment(
    counter: String,
    label: String,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = counter,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            softWrap = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailPreview() {
    UserDetail(
        profile = User.Profile(
            id = 1,
            login = "octocat",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
        ),
        details = User.Details(
            id = 1,
            login = "octocat",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            name = "The Octocat",
            email = "octocat@xyz.com",
            bio = "The Octocat is a mascot created by GitHub.",
            location = "San Francisco, CA",
            company = "GitHub",
            blog = "https://github.blog",
            followers = 100,
            following = 50,
            publicRepos = 10,
            publicGists = 5,
            createdAt = LocalDateTime.of(2011, 1, 25, 18, 44, 36),
            updatedAt = LocalDateTime.of(2011, 1, 25, 18, 44, 36),
            hireable = true,
            twitterUsername = "octocat",
        ),
        onClickBlog = {},
        onClickMail = {},
        onClickX = {},
    )
}

fun formatFollowersCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format(
            locale = Locale.UK,
            format = "%.1fM",
            count / 1_000_000.0
        )

        count >= 1_000 -> String.format(
            locale = Locale.UK,
            format = "%.1fK",
            count / 1_000.0
        )

        else -> count.toString()
    }
}
