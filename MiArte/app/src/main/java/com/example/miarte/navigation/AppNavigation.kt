package com.example.miarte.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.miarte.ui.screens.AddArtScreen
import com.example.miarte.ui.screens.AuthentificationScreen
import com.example.miarte.ui.screens.CommunicationScreen
import com.example.miarte.ui.screens.DescriptionScreen
import com.example.miarte.ui.screens.HomeScreen
import com.example.miarte.ui.screens.Art
import com.example.miarte.ui.screens.SettingsScreen


val arts = listOf(
    Art(1, "Peinture Rouge", "url1", "Alice", "Une belle peinture rouge", "100"),
    Art(2, "Musique Jazz", "url2", "Bob", "Album de jazz", "200"),
    Art(3, "Sculpture Bronze", "url3", "Charlie", "Sculpture en bronze", "150")
)

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, arts)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("authentification") {
            AuthentificationScreen(navController)
        }
        composable("communication") {
            CommunicationScreen(navController)
        }
        composable("add_art") {
            AddArtScreen(navController)
        }
        composable(
            "art_description/{artId}",
            arguments = listOf(navArgument("artId") { type = NavType.IntType })
        ) { backStackEntry ->
            val artId = backStackEntry.arguments?.getInt("artId")
            val art = arts.first { it.id == artId }
            DescriptionScreen(art, navController)
        }
    }
}
