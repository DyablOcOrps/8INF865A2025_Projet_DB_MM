package com.example.miarte.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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

    BaseScreen(navController) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.fillMaxSize()) {

                CategoryList(viewModel)

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(arts) { art ->
                        ArtCard(art, navController)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                ButtonAdd(navController)
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
    Surface(
        color = GreenCategoryList,
        modifier = modifier.fillMaxWidth() // Pour prendre toute la largeur dispo
    ) {
        LazyRow(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category,
                    onClick = { viewModel.selectCategory(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: Category,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
    ) {
        Text(
            text = category.name,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ButtonAdd(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate("add_art") },
        containerColor = GreenButton,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Text(
            text = "+",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ArtCard(art: Art, navController: NavController, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                navController.navigate("art_description/${art.id}")
            },
        colors = CardDefaults.cardColors(containerColor = GreenButton)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Auteur: ${art.author}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            Text("Prix: ${art.price} €", fontSize = 14.sp, color = Color.White)
        }
    }
}