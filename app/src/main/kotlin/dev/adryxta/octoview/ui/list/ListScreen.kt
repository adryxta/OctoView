package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.adryxta.octoview.data.UserLogin

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    onProfileClick: (login: UserLogin) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            uiState.users.forEach { profile ->
                Surface(onClick = {
                    onProfileClick(profile.login)
                }) {
                    Card(modifier = Modifier.padding(16.dp)) {
                        Text(profile.login)
                    }
                }
            }
        }
    }
}