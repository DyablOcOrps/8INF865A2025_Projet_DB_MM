package com.example.miarte.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BaseScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    isConnexionPage: Boolean = true,
    isMessagePage: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarApp(navController, isConnexionPage = isConnexionPage, isMessagePage = isMessagePage)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
