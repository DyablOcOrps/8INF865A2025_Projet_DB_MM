package com.example.miarte.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun MyArtsScreen(
    navController: NavController,
    viewModel: MiArteViewModel
) {
    // 1. Observer la liste d'oeuvres de l'utilisateur
    val myArts by viewModel.myArts.collectAsState()

    // 2. Déclencher le chargement des données
    LaunchedEffect(Unit) {
        // Appelle la fonction de filtre dès que l'écran apparaît
        viewModel.fetchMyArts()
    }

    BaseScreen(navController = navController, viewModel = viewModel, isMyArtsPage = false) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Mes Œuvres",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 3. Affichage de la liste
            if (myArts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Vous n'avez pas encore ajouté d'œuvre.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(myArts) { art ->
                        Image(
                            painter = rememberAsyncImagePainter(art.imageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
            }
        }
    }
}