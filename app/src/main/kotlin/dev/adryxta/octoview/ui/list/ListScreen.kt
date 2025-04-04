package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.common.TopBar

@Composable
fun ListScreen(
    uiState: ListUiState,
    onProfileClick: (login: User.Profile) -> Unit,
    fetchMore: () -> Unit,
    onRefresh: () -> Unit,
) {

    Scaffold(
        topBar = {
            TopBar(
                title = "GitHub Users",
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.github_mark),
                        modifier = Modifier
                            .size(56.dp)
                            .padding(12.dp),
                        contentDescription = null,
                    )
                },
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

@Composable
fun ErrorItem(
    message: String,
) {
    Box{
        Text(text = message)
    }
}