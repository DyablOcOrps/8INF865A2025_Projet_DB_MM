package com.example.miarte.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.viewmodel.MiArteViewModel
import com.example.miarte.model.Category
import com.example.miarte.ui.theme.GreenButton
import com.example.miarte.ui.theme.GreenCategoryList
import com.example.miarte.ui.theme.GreenTopBar
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArtScreen(navController: NavController, viewModel: MiArteViewModel = viewModel()) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val categories = viewModel.categoriesNoAll
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Launcher pour ouvrir la galerie d'image du téléphone
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) imageUri = uri
    }

    // Launcher pour la caméra et capture d'image
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val file = File(context.cacheDir, "captured_image_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            imageUri = Uri.fromFile(file)
        }
    }

    // Vérifie si tout les champs sont complétés avant de soumettre l'oeuvre
    val allFieldsFilled = title.isNotBlank() &&
            description.isNotBlank() &&
            price.isNotBlank() &&
            imageUri != null &&
            selectedCategory != null

    // Soumission de l'art
    fun submitArt() {
        if (imageUri != null) {
            viewModel.addArt(
                title = title,
                description = description,
                price = price,
                imageUri = imageUri!!,
                category = selectedCategory!!
            )
            navController.navigate("home")
        }
    }

    BaseScreen(navController, viewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Zone pour ajouter l'image (gallerie ou photo)
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

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                ) {
                    Text("Galerie")
                }
                Button(
                    onClick = { cameraLauncher.launch() },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
                ) {
                    Text("Caméra")
                }
            }

            // Zone pour écrire le titre
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titre de l'œuvre") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            // Zone pour écrire la description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            // Zone pour sélectionner la catégorie
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
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
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

            // Zone pour sélectionner le prix
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Prix (€)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { if (allFieldsFilled) submitArt() }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            // Bouton pour continuer
            Button(
                onClick = { submitArt() },
                enabled = allFieldsFilled,
                colors = ButtonDefaults.buttonColors(containerColor = GreenTopBar),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("Continuer")
            }

            // bouton pour abandonner l'ajout
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = GreenButton)
            ) {
                Text("Retour")
            }
        }
    }
}

