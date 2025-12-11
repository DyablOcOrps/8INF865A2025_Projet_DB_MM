package com.example.miarte.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.miarte.ui.screens.AddArtScreen
import com.example.miarte.ui.screens.AuthentificationScreen
import com.example.miarte.ui.screens.DescriptionScreen
import com.example.miarte.ui.screens.HomeScreen
import com.example.miarte.ui.screens.MyArtsScreen
import com.example.miarte.ui.screens.RegisterScreen
import com.example.miarte.ui.screens.SettingsScreen
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val viewModel: MiArteViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {

        // Route vers le menu principal
        composable("home") {
            HomeScreen(navController, viewModel)
        }

        // Route vers les paramètres
        composable("settings") {
            SettingsScreen(navController, viewModel)
        }

        // Route vers l'authentification
        composable("authentification") {
            AuthentificationScreen(navController, viewModel)
        }

        // Route vers la page d'ajout d'oeuvre
        composable("add_art") {
            AddArtScreen(navController, viewModel)
        }

        // Route vers la page d'inscription
        composable("register") {
            RegisterScreen(navController, viewModel)
        }

        // Route vers la description de l'oeuvre
        composable(
            "art_description/{artId}",
            arguments = listOf(navArgument("artId") { type = NavType.StringType })
        ) { backStackEntry ->
            val artId = backStackEntry.arguments?.getString("artId") ?: return@composable

            // Liste complète des oeuvres
            val allArts by viewModel.allArts.collectAsState()

            // Oeuvre correspondant à artId
            val art = allArts.find { it.id == artId }

            // Affichage de la description de l'art
            if (art != null) {
                DescriptionScreen(
                    art = art,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }

        // Route vers la liste de nos arts
        composable("myarts") {
            MyArtsScreen(navController, viewModel)
        }
    }
}
