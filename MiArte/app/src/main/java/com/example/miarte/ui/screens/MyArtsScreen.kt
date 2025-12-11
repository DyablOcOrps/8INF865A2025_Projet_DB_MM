package com.example.miarte.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable // <--- N'oubliez pas cet import
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card // Optionnel : pour faire plus joli
import androidx.compose.material3.CardDefaults // Optionnel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
                        // J'ai ajouté une Card pour que ce soit plus joli (bords arrondis, ombre légère)
                        // mais vous pouvez mettre le .clickable directement sur l'Image si vous préférez.
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                                // 👇 C'est ICI que se fait la navigation 👇

                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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