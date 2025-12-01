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
import androidx.navigation.NavController
import com.example.miarte.ui.theme.GreenTopBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.rotate
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun TopBarApp(navController: NavController,
              viewModel: MiArteViewModel,
              modifier: Modifier = Modifier,
              isConnexionPage: Boolean = true,
              isMessagePage: Boolean = true) {

    val isUserLoggedIn = viewModel.isUserLoggedIn.collectAsState()

    Surface(
        color = GreenTopBar,
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
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 15.dp)
            )

            // 🔹 Bouton d'identification à droite
            if (isConnexionPage) {
                Button(
                    onClick = {
                        if (isUserLoggedIn.value) {
                            navController.navigate("settings")
                        } else {
                            navController.navigate("authentification")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenTopBar),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 36.dp)
                ) {
                    Icon(
                        imageVector = if (isUserLoggedIn.value)
                            Icons.Filled.Settings
                        else
                            Icons.Filled.AccountCircle,
                        contentDescription = "Identification",
                        tint = Color.White,
                    )
                }
            }

            // 🔹 Bouton des messages à gauche
            if (isMessagePage) {
                Button(
                    onClick = { navController.navigate("communication") },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenTopBar),
                    modifier = Modifier
                        .align(Alignment.TopStart)  // placé à gauche et en haut
                        .padding(top = 36.dp)     // descend un peu le bouton
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Messages",
                        tint = Color.White,
                        modifier = Modifier.rotate(-20f)
                    )
                }
            }
        }
    }
}
