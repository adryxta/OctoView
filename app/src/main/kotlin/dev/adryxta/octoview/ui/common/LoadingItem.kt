package dev.adryxta.octoview.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CircularProgressIndicator()
    }
}