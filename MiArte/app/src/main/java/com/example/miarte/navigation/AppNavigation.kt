package com.example.miarte.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.miarte.ui.screens.MyArtsScreen
import com.example.miarte.ui.screens.RegisterScreen
import com.example.miarte.ui.screens.SettingsScreen
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val viewModel: MiArteViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("settings") {
            SettingsScreen(navController, viewModel)
        }
        composable("authentification") {
            AuthentificationScreen(navController, viewModel)
        }
        composable("communication") {
            CommunicationScreen(navController, viewModel)
        }
        composable("add_art") {
            AddArtScreen(navController, viewModel)
        }
        composable("register") {
            RegisterScreen(navController, viewModel)
        }

        composable(
            "art_description/{artId}",
            arguments = listOf(navArgument("artId") { type = NavType.StringType })
        ) { backStackEntry ->
            val artId = backStackEntry.arguments?.getString("artId") ?: return@composable

            // CORRECTION ICI 👇

            // 1. On observe la liste complète en temps réel
            // (Note : assurez-vous d'avoir fait l'étape 1 dans le ViewModel)
            val allArts by viewModel.allArts.collectAsState()

            // 2. On cherche l'art correspondant dans la liste
            val art = allArts.find { it.id == artId }

            // 3. Affichage conditionnel
            if (art != null) {
                // Si on a trouvé l'art, on l'affiche
                DescriptionScreen(
                    art = art,
                    viewModel = viewModel,
                    navController = navController
                )
            } else {
                // Si l'art est null (en cours de chargement ou ID invalide),
                // on affiche une roue de chargement au lieu d'un écran blanc.
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        composable(
            "conversation/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ConversationScreen(navController, username)
        }

        composable("myarts") {
            MyArtsScreen(navController, viewModel)
        }
    }
}
