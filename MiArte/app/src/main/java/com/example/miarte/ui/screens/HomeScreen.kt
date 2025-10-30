package com.example.miarte.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.miarte.ui.components.BaseScreen

@Composable
fun HomeScreen(navController: NavController) {
    BaseScreen(
        navController
    ) {
        CategoryList()
        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Aller aux paramètres")
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ButtonAdd(navController)
        }
    }
}

@Composable
fun CategoryList(modifier: Modifier = Modifier) {
    val categories = listOf("Dessin", "Musique", "Peinture", "Écriture", "Jeux Vidéos")

    LazyRow(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
            )
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Button(
        onClick = { /* TODO : action de connexion */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
    ) {
        Text(
            text = category,
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
        containerColor = Color.Gray,
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
