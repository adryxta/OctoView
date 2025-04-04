package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.common.LoadingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(
    modifier: Modifier,
    users: List<User.Profile>,
    onClick: (user: User.Profile) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    fetchMore: () -> Unit,
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        state = state,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
        indicator = {
            Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = state
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(minSize = 156.dp),
            contentPadding = PaddingValues(32.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
        ) {
            items(users) { user ->
                UserCard(
                    userAvatarUrl = user.avatarUrl,
                    username = user.login,
                    onClick = { onClick(user) }
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingItem(onVisible = { fetchMore() })
            }
        }
    }
}

@Preview(showSystemUi = true)
@PreviewScreenSizes
@Composable
private fun UserListScreenPreview() {
    UserList(
        users = listOf(
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
            ),
            User.Profile(
                login = "octocat",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                id = 100,
            )
        ),
        onClick = {},
        modifier = Modifier.padding(20.dp), // Add padding to the preview,
        isRefreshing = false,
        onRefresh = {},
        fetchMore = {}
    )
}
