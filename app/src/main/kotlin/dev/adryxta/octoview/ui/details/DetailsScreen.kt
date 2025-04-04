package dev.adryxta.octoview.ui.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    onBackClick: () -> Boolean
) {
    val uiState by viewModel.uiState.collectAsState()
    UserDetail(
        profile = uiState.profile,
        details = uiState.details,
        onClickBack = { onBackClick() },
        onClickBlog = {},
        onClickMail = {},
        onClickX = {},
        onClickVisitProfile = {},
    )
}