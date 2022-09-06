package no.elkbender.snake.activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.elkbender.snake.navigation.Screen
import no.elkbender.snake.screen.AboutScreen
import no.elkbender.snake.screen.HighScoreScreen
import no.elkbender.snake.screen.MenuScreen
import no.elkbender.snake.screen.SettingsScreen

class MainActivity : BaseActivity() {
    private lateinit var navController: NavHostController

    @Composable
    override fun Content() {
        navController = rememberNavController()
        SetupNavigation()
    }

    @Composable
    private fun SetupNavigation() {
        NavHost(navController = navController, startDestination = Screen.Menu.route) {
            composable(Screen.Menu.route) { MenuScreen(navController) }
            composable(Screen.HighScores.route) { HighScoreScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.About.route) { AboutScreen(navController) }
        }
    }
}
