package com.example.miarte.viewmodel

import androidx.lifecycle.ViewModel
import com.example.miarte.model.Art
import com.example.miarte.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Un petit état pour gérer le résultat de la connexion dans l'UI
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class MiArteViewModel : ViewModel() {

    // ===== FIREBASE AUTH =====
    private val firebaseAuth = FirebaseAuth.getInstance()

    // État de l'authentification observable par les écrans
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Fonction de Connexion
    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Erreur inconnue")
            }
    }

    // Fonction d'Inscription
    fun register(email: String, pass: String, firstName: String) {
        _authState.value = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { authResult ->
                // Optionnel : Ajouter le prénom au profil Firebase
                val user = authResult.user
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName)
                    .build()
                user?.updateProfile(profileUpdates)

                _authState.value = AuthState.Success
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Erreur inconnue")
            }
    }

    // Réinitialiser l'état (utile quand on quitte l'écran)
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    // ===== CATEGORIES (Ton code existant) =====
    private val allCategory = Category(0, "Tous")
    private val _categories = listOf(
        allCategory,
        Category(1, "Peinture"),
        Category(2, "Musique"),
        Category(3, "Écriture"),
        Category(4, "Dessin"),
        Category(5, "Jeux Vidéos")
    )
    val categoriesNoAll: List<Category> = _categories.filter { it.id != 0 }
    val categories: List<Category> = _categories
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    // ===== ARTS (Ton code existant) =====
    private val fullArtList = mutableListOf(
        Art(1, "Peinture Rouge", "", "Alice", "Une peinture rouge", "100", _categories[1]),
        Art(2, "Jazz Studio", "", "Bob", "Album jazz", "70", _categories[2]),
        Art(3, "Roman court", "", "Claire", "Petite histoire", "15", _categories[3])
    )
    private val _arts = MutableStateFlow<List<Art>>(fullArtList)
    val arts: StateFlow<List<Art>> = _arts.asStateFlow()

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
        _arts.value = if (category.id == 0) fullArtList else fullArtList.filter { it.category.id == category.id }
    }

    fun addArt(title: String, description: String, price: String, imageUrl: String, category: Category) {
        val newId = (fullArtList.maxOfOrNull { it.id } ?: 0) + 1
        val newArt = Art(newId, title, imageUrl, "Utilisateur", description, price, category)
        fullArtList.add(newArt)
        _arts.value = fullArtList.toList()
    }

    fun getArtById(id: Int): Art? {
        return fullArtList.firstOrNull { it.id == id }
    }
}