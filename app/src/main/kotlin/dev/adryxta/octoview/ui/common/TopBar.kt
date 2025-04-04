package dev.adryxta.octoview.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.adryxta.octoview.R

@Composable
fun TopBar(
    title: String,
    navigationIcon: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .windowInsetsPadding(
                insets = WindowInsets.systemBars
            )
    ) {
        navigationIcon.invoke()
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            textAlign = TextAlign.Center,
            lineHeight = 56.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    TopBar(
        title = "GitHub Users",
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.github_mark),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
        }
    )
}