package com.example.miarte.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.viewmodel.MiArteViewModel
import com.example.miarte.model.Category
import com.example.miarte.ui.theme.GreenButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArtScreen(navController: NavController, viewModel: MiArteViewModel = viewModel()) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var expertChecked by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val categories = viewModel.categoriesNoAll
    val context = LocalContext.current

    // === IMAGE PICKERS ===
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val uri = Uri.parse(
                ImageRequest.Builder(context).data(bitmap).build().data.toString()
            )
            imageUri = uri
        }
    }

    val allFieldsFilled = title.isNotBlank() &&
            description.isNotBlank() &&
            price.isNotBlank() &&
            imageUri != null &&
            selectedCategory != null &&
            expertChecked

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        BaseScreen(
            navController = navController,
            viewModel = viewModel,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // IMAGE
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(Color.LightGray, CircleShape)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier.size(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("📸 Ajouter une image", fontWeight = FontWeight.SemiBold)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                    ) {
                        Text("Galerie")
                    }
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                    ) {
                        Text("Caméra")
                    }
                }

                // FORM FIELDS
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre de l'œuvre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                // === CATÉGORIE ===
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Catégorie") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.name) },
                                onClick = {
                                    selectedCategory = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // PRIX + CHECK
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Prix (€)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = { expertChecked = !expertChecked },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (expertChecked) Color(0xFF4CAF50) else Color.Gray
                        )
                    ) {
                        Text(if (expertChecked) "Validé ✅" else "Check")
                    }
                }

                // SUBMIT BUTTON
                Button(
                    onClick = {
                        viewModel.addArt(
                            title = title,
                            description = description,
                            price = price,
                            imageUrl = imageUri.toString(),
                            category = selectedCategory!!
                        )
                        navController.navigate("home")
                    },
                    enabled = allFieldsFilled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text("Continuer")
                }

                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                ) {
                    Text("Retour")
                }
            }
        }
    }
}
