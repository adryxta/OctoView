package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.model.User
import dev.adryxta.octoview.ui.common.ErrorItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    onProfileClick: (login: User.Profile) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val onRefresh = { viewModel.reload() }
    val fetchMore = { viewModel.loadUsers() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.main_screen_title)
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.github_mark),
                        modifier = Modifier
                            .size(56.dp)
                            .padding(12.dp),
                        contentDescription = stringResource(R.string.cd_ic_github),
                    )
                },
                modifier = Modifier.shadow(4.dp)
            )
        },
        content = {
            when (val state = uiState) {
                is ListUiState.Error -> ErrorItem(
                    errorCode = state.error,
                    canRetry = true,
                    onRetry = onRefresh,
                )

                is ListUiState.Success -> {
                    UserList(
                        modifier = Modifier.padding(it),
                        users = state.users,
                        onClick = { user ->
                            onProfileClick(user)
                        },
                        isRefreshing = state.isLoading,
                        onRefresh = onRefresh,
                        fetchMore = fetchMore,
                    )
                }
            }
        }
    )
}