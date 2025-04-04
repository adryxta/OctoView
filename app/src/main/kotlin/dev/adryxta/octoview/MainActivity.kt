package dev.adryxta.octoview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import dev.adryxta.octoview.ui.details.DetailsScreen
import dev.adryxta.octoview.ui.list.ListScreen
import dev.adryxta.octoview.ui.theme.OctoViewTheme
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            OctoViewTheme {
                NavHost(navController = navController, startDestination = Routes.List) {
                    composable<Routes.List> {
                        ListScreen(onProfileClick = { login ->
                            navController.navigate(Routes.Details(login))
                        })
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
        data class Details(val login: String) : Routes()
    }
}