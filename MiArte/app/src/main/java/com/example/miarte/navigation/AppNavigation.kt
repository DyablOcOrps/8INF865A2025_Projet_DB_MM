package com.example.miarte.navigation

import androidx.compose.runtime.Composable
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
            arguments = listOf(navArgument("artId") { type = NavType.StringType }) // 👈 Changement ici
        ) { backStackEntry ->

            val artId = backStackEntry.arguments?.getString("artId") ?: return@composable // 👈 Changement ici

            // ATTENTION: Assurez-vous que getArtById dans le ViewModel accepte String (fait dans l'étape 2)
            val art = viewModel.getArtById(artId)

            if (art != null) {
                DescriptionScreen(
                    art = art,
                    viewModel = viewModel,
                    navController = navController
                )
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
