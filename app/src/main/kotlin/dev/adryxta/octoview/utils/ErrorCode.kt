package dev.adryxta.octoview.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.adryxta.octoview.R
import dev.adryxta.octoview.data.api.NetworkError
import dev.adryxta.octoview.utils.ErrorCode.CONNECTION
import dev.adryxta.octoview.utils.ErrorCode.HTTP
import dev.adryxta.octoview.utils.ErrorCode.RATE_LIMIT
import dev.adryxta.octoview.utils.ErrorCode.TIMEOUT
import dev.adryxta.octoview.utils.ErrorCode.UNKNOWN

enum class ErrorCode {
    CONNECTION,
    TIMEOUT,
    RATE_LIMIT,
    HTTP,
    UNKNOWN;
}

@Composable
fun ErrorCode.errorMessage(): String {
    val resId = when (this) {
        CONNECTION -> R.string.connection_error
        TIMEOUT -> R.string.timeout_error
        RATE_LIMIT -> R.string.rate_limit_error
        HTTP -> R.string.http_error
        UNKNOWN -> R.string.unknown_error
    }
    return stringResource(id = resId)
}

fun Throwable.errorCode(): ErrorCode {
    return when (this) {
        is NetworkError.Connection -> CONNECTION
        is NetworkError.Timeout -> TIMEOUT
        is NetworkError.RateLimit -> RATE_LIMIT
        is NetworkError.Http -> HTTP
        is NetworkError.Unknown -> UNKNOWN
        else -> UNKNOWN
    }
}