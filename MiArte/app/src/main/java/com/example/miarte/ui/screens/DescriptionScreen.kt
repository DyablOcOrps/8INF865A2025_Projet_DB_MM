package com.example.miarte.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.miarte.model.Art
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.ui.theme.GreenButton
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun DescriptionScreen(
    art: Art,
    viewModel: MiArteViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val isOwner = viewModel.isArtOwner(art)

    BaseScreen(navController, viewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = art.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Image(
                painter = rememberAsyncImagePainter(art.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Auteur: ${art.author}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Description: ${art.description}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Prix: ${art.price} €", fontSize = 16.sp)

            Spacer(modifier = Modifier.weight(1f))

            // --- ZONE DES BOUTONS ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Bouton Retour (Toujours visible)
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Retour")
                }

                // 2. Logique d'affichage conditionnel
                if (isOwner) {
                    // Si je suis le propriétaire -> Bouton Supprimer
                    Button(
                        onClick = {
                            viewModel.deleteArt(
                                art = art,
                                onSuccess = {
                                    Toast.makeText(context, "Œuvre supprimée !", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onError = { errorMsg ->
                                    Toast.makeText(context, "Erreur : $errorMsg", Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Supprimer")
                    }
                } else {
                    // Si je NE suis PAS le propriétaire -> Bouton Payer / Contacter
                    Button(
                        onClick = {
                            viewModel.sendPurchaseNotification(
                                art = art,
                                onSuccess = {
                                    Toast.makeText(context, "Notification envoyée au propriétaire !", Toast.LENGTH_LONG).show()
                                },
                                onError = { errorMsg ->
                                    Toast.makeText(context, "Erreur : $errorMsg", Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                    ) {
                        Text("Acheter / Contacter")
                    }
                }
            }
        }
    }
}