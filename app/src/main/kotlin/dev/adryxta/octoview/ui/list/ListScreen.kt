package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.UserLogin
import dev.adryxta.octoview.ui.common.TopBar

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    onProfileClick: (login: UserLogin) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
            UserList(
                uiState = uiState,
                modifier = Modifier.padding(it),
                users = uiState.users,
                onClick = { user ->
                    onProfileClick(user.login)
                }
            )
        }
    )
}