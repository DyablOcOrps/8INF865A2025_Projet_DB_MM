package com.example.miarte.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.miarte.ui.components.TopBarApp

@Composable
fun CommunicationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBarApp(navController)

        // 🔹 Contenu principal sous la barre
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp), // un peu d’espace
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Bienvenue sur la page des messages",
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text("Retour à l'accueil")
            }
        }
    }
}