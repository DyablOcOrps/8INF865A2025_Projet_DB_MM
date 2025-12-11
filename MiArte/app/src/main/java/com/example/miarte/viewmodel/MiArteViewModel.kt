package com.example.miarte.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miarte.model.Art
import com.example.miarte.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore // NOUVEAU
import com.google.firebase.firestore.toObject // NOUVEAU
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.* // combine, stateIn, SharedFlow, etc.
import java.util.UUID

// État de l'authentification (inchangé)
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class MiArteViewModel : ViewModel() {

    // ===== FIREBASE SETUP =====
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance() // NOUVEAU: Instance Firestore

    // ===== FIREBASE AUTH (INCHANGÉ) =====
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
                _isUserLoggedIn.value = true
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Erreur inconnue")
            }
    }

    fun register(email: String, pass: String, firstName: String) {
        _authState.value = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName)
                    .build()
                user?.updateProfile(profileUpdates)

                _authState.value = AuthState.Success
                _isUserLoggedIn.value = true
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Erreur inconnue")
            }
    }

    fun logout() {
        firebaseAuth.signOut()
        _isUserLoggedIn.value = false
    }

    fun deleteAccount(onResult: (Boolean, String?) -> Unit) {
        val user = firebaseAuth.currentUser
        if (user == null) {
            onResult(false, "Utilisateur non connecté")
            return
        }
        user.delete()
            .addOnSuccessListener {
                _isUserLoggedIn.value = false
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    // ===== CATEGORIES =====
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

    // État de la catégorie sélectionnée
    private val _selectedCategory = MutableStateFlow<Category?>(allCategory)
    val selectedCategory = _selectedCategory.asStateFlow()

    // ===== ARTS (Logique Firestore) =====

    // 1. Stocke TOUTES les oeuvres venant de Firestore (Source de vérité)
    private val _allArtsFromFirestore = MutableStateFlow<List<Art>>(emptyList())

    // 2. LISTE PUBLIQUE (arts) : combine les données brutes et le filtre de catégorie
    val arts: StateFlow<List<Art>> = _allArtsFromFirestore
        .combine(_selectedCategory) { allArts, selectedCat ->
            // Filtre : si selectedCat est null ou si c'est la catégorie "Tous", on renvoie tout.
            if (selectedCat == null || selectedCat.id == 0) {
                allArts
            } else {
                // Sinon, on filtre par l'ID de la catégorie
                allArts.filter { it.category.id == selectedCat.id }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Maintient le Flow actif
            initialValue = emptyList()
        )

    init {
        // Lance l'écoute Firestore au démarrage du ViewModel
        fetchArtsRealtime()
    }

    // --- LECTURE : Écoute en temps réel de Firestore ---
    private fun fetchArtsRealtime() {
        db.collection("arts")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MiArteViewModel", "Erreur écoute Firestore", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val artList = snapshot.documents.mapNotNull { doc ->
                        // Conversion du document en objet Art. On force l'ID du document (String) dans l'objet Art.
                        doc.toObject<Art>()?.copy(id = doc.id)
                    }
                    _allArtsFromFirestore.value = artList
                }
            }
    }

    // StateFlow qui contiendra la liste filtrée
    private val _myArts = MutableStateFlow<List<Art>>(emptyList())
    val myArts: StateFlow<List<Art>> = _myArts.asStateFlow()

    fun fetchMyArts() {
        // 1. Récupère l'UID de l'utilisateur actuel
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId.isNullOrEmpty()) {
            // Si l'utilisateur n'est pas connecté, la liste est vide.
            _myArts.value = emptyList()
            return
        }

        // 2. Requête Firestore avec filtre
        db.collection("arts")
            // Filtrer les documents où le champ "userId" est égal à l'UID de l'utilisateur connecté
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Gérer l'erreur, par exemple : Log.e("MiArte", "Erreur lors du fetch : $error")
                    return@addSnapshotListener
                }

                val artsList = snapshot?.toObjects(Art::class.java) ?: emptyList()
                _myArts.value = artsList // Met à jour le StateFlow
            }
    }

    // --- ACTION : Sélection de Catégorie (simplifié grâce à 'combine') ---
    fun selectCategory(category: Category) {
        // Si on clique sur la catégorie déjà active, on désélectionne (revient à "Tous")
        if (_selectedCategory.value?.id == category.id) {
            _selectedCategory.value = allCategory
        } else {
            // Sinon, on applique la nouvelle catégorie
            _selectedCategory.value = category
        }
    }

    // --- ÉCRITURE : Ajout d'une œuvre AVEC UPLOAD D'IMAGE ---
    fun addArt(title: String, description: String, price: String, imageUri: Uri, category: Category) {
        val authorName = firebaseAuth.currentUser?.displayName ?: "Anonyme"
        val currentUserId = firebaseAuth.currentUser?.uid ?: return

        // 1. Créer une référence unique pour l'image dans Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        // 2. Envoyer le fichier (Upload)
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            // 3. Succès de l'upload -> On demande l'URL de téléchargement publique
            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                // 4. On crée l'objet Art avec la VRAIE URL internet (https://...)
                val newArt = Art(
                    id = "",
                    title = title,
                    imageUrl = downloadUrl.toString(), // C'est ici que la magie opère !
                    userId = currentUserId,
                    author = authorName,
                    description = description,
                    price = price,
                    category = category
                )

                // 5. On sauvegarde dans Firestore
                db.collection("arts")
                    .add(newArt)
                    .addOnSuccessListener { Log.d("MiArteViewModel", "Oeuvre ajoutée avec succès") }
                    .addOnFailureListener { e -> Log.w("MiArteViewModel", "Erreur ajout Firestore", e) }
            }
        }.addOnFailureListener { e ->
            Log.e("MiArteViewModel", "Erreur lors de l'upload de l'image", e)
        }
    }

    // --- LECTURE PAR ID : Recherche locale (dans le cache du Flow) ---
    // ATTENTION : L'ID est maintenant un String !
    fun getArtById(id: String): Art? {
        return _allArtsFromFirestore.value.firstOrNull { it.id == id }
    }
}