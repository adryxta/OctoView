package dev.adryxta.octoview.ui.details

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.common.TopBar
import timber.log.Timber
import java.time.LocalDateTime

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
    onClickBack: () -> Unit = {},
    onClickBlog: () -> Unit = {},
    onClickMail: () -> Unit = {},
    onClickX: () -> Unit = {},
    onClickVisitProfile: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(
                title = profile.login,
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
                        text = "Visit Profile On Web",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AsyncImage(
                        model = profile.avatarUrl,
                        contentDescription = "avatar",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(150.dp)
                    )
                    if (details != null) {
                        UserDetailTopGridComponent(details)
                    }

                }
                details?.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        maxLines = 1,
                    )
                }
                details?.bio?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
                details?.company?.let {
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
                details?.location?.let {
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
                details?.blog?.takeIf { it.isNotEmpty() }?.let { //removes both null and empty blogs value
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
                    details?.email?.let {
                        IconButton(
                            onClick = onClickMail,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_mail),
                                contentDescription = null,
                            )
                        }
                    }
                    details?.twitterUsername?.let {
                        IconButton(
                            onClick = onClickX,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_twitter_x),
                                contentDescription = null,
                            )
                        }
                    }
                    details?.hireable?.let {
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
    )
}

@Composable
fun UserDetailTopGridComponent(
    details: User.Details
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
    ) {
        item {
            GridSegment(
                counter = details.followers,
                label = "Followers"
            )
        }
        item {
            GridSegment(
                counter = details.following,
                label = "Following"
            )
        }
        item {
            GridSegment(
                counter = details.publicRepos,
                label = "Public Repos"
            )
        }
        item {
            GridSegment(
                counter = details.publicGists,
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
private fun UserDetailLoadingPreview() {
    UserDetail(
        profile = User.Profile(
            id = 1,
            login = "octocat",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
        )
    )
}

@Preview
@Composable
private fun UserDetailErrorPreview() {
    UserDetail(
        profile = User.Profile(
            id = 1,
            login = "octocat",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
        )
    )
}

@Preview
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
    )
}

fun Modifier.shimmerEffect(until: () -> Boolean): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        )
    )

    if (until()) {
        background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFEEEEEE),
                    Color(0xFFDDDDDD),
                    Color(0xFFEEEEEE),
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            )
        ).onGloballyPositioned {
            size = it.size
        }
    }
    this@shimmerEffect
}

@Composable
fun DpUnit(textUnit: TextUnit) = with(LocalDensity.current) {
    Timber.d("textUnit: $textUnit")
    Timber.d("textUnit to Dp: ${textUnit.toDp()}")
    textUnit.toDp()
}