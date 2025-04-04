package dev.adryxta.octoview.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.common.TopBar
import java.time.LocalDateTime

@Composable
fun UserDetail(
    user: User.Details,
    onClickBack: () -> Unit,
    onClickBlog: () -> Unit,
    onClickMail: () -> Unit,
    onClickX: () -> Unit,
    onClickVisitProfile: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = user.login,
                navigationIcon = {
                    IconButton(
                        onClick = onClickBack,
                        modifier = Modifier
                            .size(56.dp)
                            .padding(10.dp),
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.back_nav),
                                contentDescription = null,
                            )
                        }
                    )
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onClickVisitProfile,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                content = {
                    Text(
                        text = "Visit Profile",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                }
            )
        },
        content = {
            UserDetailContent(
                modifier = Modifier.padding(it),
                user = user,
                onClickBlog = onClickBlog,
                onClickMail = onClickMail,
                onClickX = onClickX,
            )
        }
    )
}

@Composable
fun UserDetailContent(
    modifier: Modifier,
    user: User.Details,
    onClickBlog: () -> Unit,
    onClickMail: () -> Unit,
    onClickX: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp)
            )
            UserDetailTopGridComponent(user)
        }
        user.name?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                maxLines = 1,
            )
        }
        user.bio?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
        user.company?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.office),
                    contentDescription = null,
                )
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    maxLines = 1,
                )
            }
        }
        user.location?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.location),
                    contentDescription = null,
                )
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    maxLines = 1,
                )
            }
        }
        user.blog?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .clickable {
                        onClickBlog.invoke()
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.link),
                    contentDescription = null,
                )
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    maxLines = 1,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            user.email?.let{
                IconButton(
                    onClick = onClickMail,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_mail),
                        contentDescription = null,
                    )
                }
            }
            user.twitterUsername?.let {
                IconButton(
                    onClick = onClickX,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_twitter_x),
                        contentDescription = null,
                    )
                }
            }
            user.hireable?.let {
                Icon(
                    painter = painterResource(R.drawable.online),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 10.dp, end = 5.dp),
                    tint = Color.Green
                )
                Text(
                    text = "Available for hire",
                )
            }
        }
    }
}

@Composable
fun UserDetailTopGridComponent(
    user: User.Details
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
    ) {
        item {
            GridSegment(
                counter = user.followers,
                label = "Followers"
            )
        }
        item {
            GridSegment(
                counter = user.following,
                label = "Following"
            )
        }
        item {
            GridSegment(
                counter = user.publicRepos,
                label = "Public Repos"
            )
        }
        item {
            GridSegment(
                counter = user.publicGists,
                label = "Public Gists"
            )
        }
    }
}

@Composable
fun GridSegment(
    counter: Int,
    label: String,
) {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$counter",
            modifier = Modifier
                .padding(10.dp),
        )
        Text(
            text = label
        )
    }
}

@Preview
@Composable
private fun UserDetailPreview() {
    UserDetail(
        user = User.Details(
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
            createdAt = LocalDateTime.of(2011,1,25,18,44,36),
            updatedAt = LocalDateTime.of(2011,1,25,18,44,36),
            hireable = true,
            twitterUsername = "octocat",
        ),
        onClickBack = {},
        onClickBlog = {},
        onClickMail = {},
        onClickX = {},
        onClickVisitProfile = {},
    )
}