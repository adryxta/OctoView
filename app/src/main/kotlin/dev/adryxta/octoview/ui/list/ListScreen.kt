package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.common.ErrorItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    uiState: ListUiState,
    onProfileClick: (login: User.Profile) -> Unit,
    fetchMore: () -> Unit,
    onRefresh: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "GitHub Users"
                    ) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.github_mark),
                        modifier = Modifier
                            .size(56.dp)
                            .padding(12.dp),
                        contentDescription = null,
                    )
                },
                modifier = Modifier.shadow(4.dp)
            )
        },
        content = {
            when (uiState) {
                is ListUiState.Error -> ErrorItem(
                    message = uiState.error?.message?: "Unknown error",
                )
                is ListUiState.Success -> {
                    UserList(
                        modifier = Modifier.padding(it),
                        users = uiState.users,
                        onClick = { user ->
                            onProfileClick(user)
                        },
                        isRefreshing = uiState.isLoading,
                        onRefresh = onRefresh,
                        fetchMore = fetchMore,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun ListScreenPreview() {
    ListScreen(
        uiState = ListUiState.Success(
            users = listOf(
                User.Profile(
                    id = 1,
                    login = "adryxta",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1234567?v=4",
                ),
                User.Profile(
                    id = 2,
                    login = "octocat",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1234567?v=4",
                ),
            ),
            isLoading = false,
        ),
        onProfileClick = { },
        fetchMore = { },
        onRefresh = { },
    )
}