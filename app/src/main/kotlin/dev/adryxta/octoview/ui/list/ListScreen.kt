package dev.adryxta.octoview.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.adryxta.octoview.data.UserLogin

@Composable
fun ListScreen(
    onProfileClick: (login: UserLogin) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text("List Screen")
            Button(onClick = {
                onProfileClick("octocat")
            }) { }
        }
    }
}