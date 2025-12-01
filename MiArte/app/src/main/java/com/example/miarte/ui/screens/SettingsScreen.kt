package com.example.miarte.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: MiArteViewModel = viewModel()
) {
    BaseScreen(
        navController = navController,
        viewModel = viewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Page de paramètres", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Bouton Déconnexion
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("home") {
                        popUpTo("settings") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Se déconnecter")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Bouton Supprimer compte
            Button(
                onClick = {
                    viewModel.deleteAccount { success, error ->
                        if (success) {
                            navController.navigate("home") {
                                popUpTo("settings") { inclusive = true }
                            }
                        } else {
                            println("Erreur suppression : $error")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Supprimer mon compte")
            }
        }
    }
}
