package com.example.miarte.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.miarte.ui.components.BaseScreen

@Composable
fun SettingsScreen(navController: NavController) {
    BaseScreen(
        title = "Paramètres",
        showButtonAdd = false // pas de bouton flottant ici
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Page de paramètres", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text("Retour à l'accueil")
            }
        }
    }
}
