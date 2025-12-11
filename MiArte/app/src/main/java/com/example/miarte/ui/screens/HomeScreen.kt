package com.example.miarte.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.viewmodel.MiArteViewModel
import com.example.miarte.model.Art
import com.example.miarte.model.Category
import com.example.miarte.ui.theme.GreenButton
import com.example.miarte.ui.theme.GreenCategoryList

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MiArteViewModel = viewModel()
) {
    val arts = viewModel.arts.collectAsState().value

    BaseScreen(navController, viewModel, isHomePage = false) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.fillMaxSize()) {

                CategoryList(viewModel)

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(arts) { art ->
                        ArtCard(art, navController, viewModel)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                ButtonAdd(navController, viewModel)
            }
        }
    }
}

@Composable
fun CategoryList(
    viewModel: MiArteViewModel,
    modifier: Modifier = Modifier
) {
    val categories = viewModel.categories
    // 💡 Récupérer la catégorie sélectionnée depuis le ViewModel
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Surface(
        color = GreenCategoryList,
        modifier = modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category,
                    // 💡 Passer la catégorie sélectionnée pour la comparaison
                    isSelected = category == selectedCategory,
                    onClick = {
                        // 💡 Logique pour désélectionner si on clique à nouveau sur la même catégorie
                        val newCategory = if (category == selectedCategory) categories.first() else category
                        viewModel.selectCategory(newCategory)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: Category,
    isSelected: Boolean, // 💡 Nouveau paramètre
    onClick: () -> Unit
) {
    // 💡 Définir la couleur du conteneur en fonction de l'état
    val containerColor = if (isSelected) {
        Color.White // Couleur pour la catégorie sélectionnée (vous pouvez la changer)
    } else {
        GreenButton // Couleur par défaut
    }

    // 💡 Définir la couleur du texte en fonction de l'état
    val textColor = if (isSelected) {
        GreenButton // Couleur du texte pour la sélection
    } else {
        Color.White // Couleur du texte par défaut
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor), // Utiliser la couleur dynamique
        // Ajouter un border si vous voulez que la distinction soit plus claire
        border = if (isSelected) ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(GreenButton)
        ) else null
    ) {
        Text(
            text = category.name,
            color = textColor, // Utiliser la couleur de texte dynamique
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ButtonAdd(
    navController: NavController,
    viewModel: MiArteViewModel
) {
    val isUserLoggedIn = viewModel.isUserLoggedIn.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        FloatingActionButton(
            onClick = {
                if (isUserLoggedIn.value) {
                    navController.navigate("add_art")
                } else {
                    showDialog = true
                }
            },
            containerColor = GreenButton
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // 🔽 Affichage de la bulle si nécessaire
    if (showDialog) {
        LoginBubbleDialog(
            onDismiss = { showDialog = false },
            onGoToLogin = {
                showDialog = false
                navController.navigate("authentification")
            },
            message = "Veuillez vous identifier pour ajouter une œuvre."
        )
    }
}

@Composable
fun LoginBubbleDialog(
    onDismiss: () -> Unit,
    onGoToLogin: () -> Unit,
    message : String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onGoToLogin) {
                Text("Se connecter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        },
        title = {
            Text("Pour continuer : connectez-vous")
        },
        text = {
            Text(message)
        }
    )
}

@Composable
fun ArtCard(
    art: Art,
    navController: NavController,
    viewModel: MiArteViewModel,
    modifier: Modifier = Modifier
) {

    val isUserLoggedIn = viewModel.isUserLoggedIn.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                if (isUserLoggedIn.value) {
                    navController.navigate("art_description/${art.id}")
                } else {
                    showDialog = true
                }
            },
        colors = CardDefaults.cardColors(containerColor = GreenButton)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(art.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Auteur: ${art.author}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            Text("Prix: ${art.price} €", fontSize = 14.sp, color = Color.White)
        }

        if (showDialog) {
            LoginBubbleDialog(
                onDismiss = { showDialog = false },
                onGoToLogin = {
                    showDialog = false
                    navController.navigate("authentification")
                },
                message = "Veuillez vous identifier pour accéder aux détails de cette œuvre."
            )
        }
    }
}