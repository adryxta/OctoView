package dev.adryxta.octoview.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.adryxta.octoview.R
import dev.adryxta.octoview.utils.ErrorCode
import dev.adryxta.octoview.utils.errorMessage

@Composable
fun ErrorItem(
    errorCode: ErrorCode,
    canRetry: Boolean = false,
    onRetry: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = errorCode.errorMessage()
            )
            if (canRetry) {
                Button(
                    onClick = onRetry,
                ) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}