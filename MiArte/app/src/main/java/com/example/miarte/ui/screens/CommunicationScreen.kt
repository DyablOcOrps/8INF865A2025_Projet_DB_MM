package com.example.miarte.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.miarte.ui.components.BaseScreen

@Composable
fun CommunicationScreen(navController: NavController) {
    val users = listOf("Alice", "Bob", "Charlie", "Diana", "Élodie", "François")

    BaseScreen(navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Messages",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Liste verticale de "contacts"
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(users) { user ->
                    Button(
                        onClick = {
                            navController.navigate("conversation/$user")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = user, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
