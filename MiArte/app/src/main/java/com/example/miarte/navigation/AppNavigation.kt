package com.example.miarte.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miarte.ui.screens.AddArtScreen
import com.example.miarte.ui.screens.AuthentificationScreen
import com.example.miarte.ui.screens.CommunicationScreen
import com.example.miarte.ui.screens.HomeScreen
import com.example.miarte.ui.screens.SettingsScreen


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
    }
}
