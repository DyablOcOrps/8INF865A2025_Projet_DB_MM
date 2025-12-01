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
import com.example.miarte.ui.screens.ConversationScreen
import com.example.miarte.ui.screens.RegisterScreen
import com.example.miarte.ui.screens.SettingsScreen
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
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
        composable("register") {
            RegisterScreen(navController)
        }
        composable(
            "art_description/{artId}",
            arguments = listOf(navArgument("artId") { type = NavType.IntType })
        ) { backStackEntry ->

            val viewModel: MiArteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            val artId = backStackEntry.arguments?.getInt("artId") ?: return@composable

            val art = viewModel.getArtById(artId)
            if (art != null) {
                DescriptionScreen(art, navController = navController)
            }
        }

        composable(
            "conversation/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ConversationScreen(navController, username)
        }
    }
}
