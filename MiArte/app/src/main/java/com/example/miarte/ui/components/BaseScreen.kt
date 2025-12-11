package com.example.miarte.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.miarte.ui.theme.GreenBackground
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: MiArteViewModel,
    modifier: Modifier = Modifier,
    isConnexionOrSettingsPage: Boolean = true,
    isHomePage: Boolean = true,
    isMyArtsPage: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize().background(GreenBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Barre commune à chaque page en haut de l'écran
            TopBarApp(
                navController = navController,
                viewModel = viewModel,
                isConnexionOrSettingsPage= isConnexionOrSettingsPage,
                isHomePage = isHomePage,
                isMyArtsPage = isMyArtsPage
            )
            content()
        }
    }
}
