package dev.adryxta.octoview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import dev.adryxta.octoview.ui.details.DetailsScreen
import dev.adryxta.octoview.ui.list.ListScreen
import dev.adryxta.octoview.ui.list.ListUiState
import dev.adryxta.octoview.ui.list.ListViewModel
import dev.adryxta.octoview.ui.theme.OctoViewTheme
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            OctoViewTheme {
                NavHost(navController = navController, startDestination = Routes.List) {
                    composable<Routes.List> {
                        val state: ListUiState by listViewModel.uiState.collectAsState()
                        ListScreen(
                            uiState = state,
                            onProfileClick = { profile ->
                            navController.navigate(
                                Routes.Details(
                                    id = profile.id,
                                    login = profile.login,
                                    avatarUrl = profile.avatarUrl
                                )
                            ) },
                            fetchMore = {listViewModel.loadUsers()},
                            onRefresh = { listViewModel.reload() },
                        )
                    }
                    composable<Routes.Details> { backStackEntry ->
                        val (_) = backStackEntry.toRoute<Routes.Details>()
                        DetailsScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    @Serializable
    sealed class Routes {

        @Serializable
        data object List : Routes()

        @Serializable
        data class Details(
            val id: Int,
            val login: String,
            val avatarUrl: String? = null,
        ) : Routes()
    }
}