package com.example.miarte.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.navigation.NavController
import com.example.miarte.ui.theme.GreenTopBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun TopBarApp(navController: NavController,
              viewModel: MiArteViewModel,
              modifier: Modifier = Modifier,
              isConnexionOrSettingsPage: Boolean = true,
              isHomePage: Boolean = true,
              isMyArtsPage: Boolean = true
    ) {

    // Vérifie si l'utilisateur est connecté
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

            // Bouton d'identification ou de paramètres (apparait seulement si on est pas dans l'une de ces pages)
            if (isConnexionOrSettingsPage) {
                IconButton(
                    onClick = {
                        // Si l'utilisateur est connecté
                        if (isUserLoggedIn.value) {
                            // Naviguer vers la page de paramètres
                            navController.navigate("settings")
                        } else {
                            // Sinon naviguer vers la page d'authentification
                            navController.navigate("authentification")
                        }
                    },
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

            // Bouton du menu d'accueil (Apparait seulement si on est pas sur le menu d'accueil)
            if (isHomePage) {
                IconButton(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 36.dp)
                        .padding(horizontal = 40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Messages",
                        tint = Color.White,
                    )
                }
            }

            // Bouton pour la page liste de mes oeuvres (Apparait seulement si on est pas sur le menu correspondant)
            if (isMyArtsPage) {
                IconButton(
                    onClick = { navController.navigate("myarts") },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArtTrack,
                        contentDescription = "MyArts",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White,

                    )
                }
            }
        }
    }
}
