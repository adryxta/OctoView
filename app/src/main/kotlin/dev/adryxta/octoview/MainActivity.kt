package dev.adryxta.octoview

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.net.toUri
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
                        ListScreen(
                            onProfileClick = { profile ->
                                navController.navigate(
                                    Routes.Details(
                                        id = profile.id,
                                        login = profile.login,
                                        avatarUrl = profile.avatarUrl
                                    )
                                )
                            },
                        )
                    }
                    composable<Routes.Details> { backStackEntry ->
                        val (_) = backStackEntry.toRoute<Routes.Details>()
                        DetailsScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onClickMail = { mail ->
                                openEmailApp(mail)
                            },
                            onClickX = { username ->
                                openXProfile(username)
                            },
                            onClickProfile = { login ->
                                openGitHubProfile(login)
                            },
                            onClickBlog = { blog ->
                                openInBrowser(blog)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun openEmailApp(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Hello!")
        }
        startActivity(Intent.createChooser(intent, "Choose an email app"))
    }

    private fun openXProfile(username: String) {
        val twitterIntent = Intent(Intent.ACTION_VIEW).apply {
            data = "x://user?screen_name=$username".toUri()
        }

        try {
            // Try opening in Twitter app
            startActivity(twitterIntent)
        } catch (_: ActivityNotFoundException) {
            // Fallback to browser if Twitter app is not installed
            openInBrowser("https://x.com/$username")
        }
    }

    private fun openGitHubProfile(login: String) {
        openInBrowser("https://github.com/$login")
    }

    private fun openInBrowser(url: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(webIntent)
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