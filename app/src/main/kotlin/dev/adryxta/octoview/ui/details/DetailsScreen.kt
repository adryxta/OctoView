package dev.adryxta.octoview.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.adryxta.octoview.R
import dev.adryxta.octoview.ui.common.ErrorItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    onBackClick: () -> Boolean,
    onClickBlog: (blog: String) -> Unit,
    onClickMail: (mail: String) -> Unit,
    onClickX: (username: String) -> Unit,
    onClickProfile: (login: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = viewModel.profile.login,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_nav),
                            contentDescription = null,
                        )
                    }
                },
                modifier = Modifier.shadow(4.dp)
            )
        },
        bottomBar = {
            Button(
                onClick = { onClickProfile.invoke(viewModel.profile.login) },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                content = {
                    Text(
                        text = stringResource(R.string.visit_profile_button_text),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                when (uiState) {
                    is DetailUiState.Error -> ErrorItem(
                        errorCode = (uiState as DetailUiState.Error).error
                    )

                    is DetailUiState.Success -> {
                        UserDetail(
                            profile = (uiState as DetailUiState.Success).profile,
                            details = (uiState as DetailUiState.Success).details,
                            onClickBlog = { blog ->
                                onClickBlog(blog)
                            },
                            onClickMail = { email ->
                                onClickMail(email)
                            },
                            onClickX = { username ->
                                onClickX(username)
                            },
                        )
                    }
                }
            }
        }
    )
}

