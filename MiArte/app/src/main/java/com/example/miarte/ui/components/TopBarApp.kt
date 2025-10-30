package com.example.miarte.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*

@Composable
fun TopBarApp(modifier: Modifier = Modifier) {
    Surface(
        color = Color.Gray,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "MiArte",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // 🔹 Bouton d'identification à droite
            Button(
                onClick = { /* TODO : action de connexion */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                modifier = Modifier
                    .align(Alignment.TopEnd)  // placé à droite et en haut
                    .padding(top = 36.dp)     // 🔽 descend un peu le bouton
            ) {
                Text(
                    text = "Connexion",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            // 🔹 Bouton des messages à gauche
            Button(
                onClick = { /* TODO : action de connexion */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                modifier = Modifier
                    .align(Alignment.TopStart)  // placé à gauche et en haut
                    .padding(top = 36.dp)     // 🔽 descend un peu le bouton
            ) {
                Text(
                    text = "Messages",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
