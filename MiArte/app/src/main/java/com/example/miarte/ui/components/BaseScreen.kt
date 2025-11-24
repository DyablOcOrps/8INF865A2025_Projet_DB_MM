package com.example.miarte.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.miarte.ui.theme.GreenBackground

@Composable
fun BaseScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    isConnexionPage: Boolean = true,
    isMessagePage: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize().background(GreenBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarApp(navController, isConnexionPage = isConnexionPage, isMessagePage = isMessagePage)
            content()
        }
    }
}
