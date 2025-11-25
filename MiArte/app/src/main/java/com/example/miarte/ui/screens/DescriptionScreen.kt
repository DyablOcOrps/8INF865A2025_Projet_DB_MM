package com.example.miarte.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.miarte.model.Art
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.ui.theme.GreenButton
import com.example.miarte.ui.theme.GreenCategoryList
import com.example.miarte.ui.theme.GreenTopBar

@Composable
fun DescriptionScreen(
    art: Art,
    navController: NavController
) {
    BaseScreen(navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = art.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Image en grand
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Auteur: ${art.author}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Description: ${art.description}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Prix: ${art.price} €", fontSize = 16.sp)

            Spacer(modifier = Modifier.weight(1f))

            // Boutons en bas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                ) {
                    Text("Retour")
                }
                Button(
                    onClick = { navController.navigate("conversation/${art.author}") },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                ) {
                    Text("Contact")
                }
            }
        }
    }
}
