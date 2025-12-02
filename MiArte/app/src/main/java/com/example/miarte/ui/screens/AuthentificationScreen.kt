package com.example.miarte.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.miarte.ui.components.BaseScreen
import com.example.miarte.ui.theme.GreenButton
import com.example.miarte.viewmodel.AuthState
import com.example.miarte.viewmodel.MiArteViewModel

@Composable
fun AuthentificationScreen(
    navController: NavController,
    viewModel: MiArteViewModel = androidx.lifecycle.viewmodel.compose.viewModel() // Initialisation par défaut
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // On observe l'état de l'authentification
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    // Réagir aux changements d'état (Succès ou Erreur)
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                viewModel.resetAuthState() // Reset pour éviter de re-naviguer si on revient en arrière
                navController.navigate("home") {
                    popUpTo("auth") { inclusive = true } // Empêche de revenir au login avec "Retour"
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetAuthState() // Reset pour permettre de ré-essayer
            }
            else -> Unit
        }
    }

    // On définit l'action de connexion ici pour la réutiliser
    val onLoginClick = {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(email, password)
        } else {
            Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
        }
    }

    BaseScreen(navController, viewModel, isConnexionOrSettingsPage = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Connexion", fontSize = 26.sp)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true, // Important pour empêcher le retour à la ligne visuel
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, // Affiche le @
                    imeAction = ImeAction.Next // Affiche la flèche "Suivant"
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done // Affiche la coche "Validation"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onLoginClick() // Lance la connexion quand on valide au clavier
                    }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(email, password)
                    } else {
                        Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenButton),
                modifier = Modifier.fillMaxWidth(),
                enabled = authState != AuthState.Loading // Désactive le bouton pendant le chargement
            ) {
                if (authState == AuthState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Se connecter")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("register") }) {
                Text("Pas de compte ? Créer un compte")
            }
        }
    }
}