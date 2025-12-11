package com.example.miarte.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.miarte.ui.theme.GreenButton
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun MyArtsScreen(
    navController: NavController,
    viewModel: MiArteViewModel
) {
    // Collecte des oeuvres de l'utilisateur
    val myArts by viewModel.myArts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMyArts()
    }

    BaseScreen(navController = navController, viewModel = viewModel, isMyArtsPage = false) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Mes Œuvres",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // S'il n'y a pas d'oeuvres
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = GreenButton)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(art.imageUrl),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                                .clickable {
                                    navController.navigate("art_description/${art.id}")
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}