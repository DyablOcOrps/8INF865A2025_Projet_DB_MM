package com.example.miarte.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miarte.model.Art
import com.example.miarte.model.Category
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.*
import java.util.UUID

// État de l'authentification
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class MiArteViewModel : ViewModel() {

    // ===== FIREBASE =====
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance() // NOUVEAU: Instance Firestore

    // ===== FIREBASE AUTH =====
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    // Authentification
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

    // Inscription
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

    // Déconnexion
    fun logout() {
        firebaseAuth.signOut()
        _isUserLoggedIn.value = false
    }

    // Suppression du compte
    fun deleteAccount(onResult: (Boolean, String?) -> Unit) {
        val user = firebaseAuth.currentUser
        val uid = user?.uid

        if (user == null || uid == null) {
            onResult(false, "Utilisateur non connecté")
            return
        }

        db.collection("arts")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { snapshot ->

                // Liste des tâches à effectuer
                val deleteTasks = mutableListOf<com.google.android.gms.tasks.Task<Void>>()

                for (document in snapshot.documents) {
                    val artUrl = document.getString("imageUrl")

                    // Tache de suppression de l'image
                    if (artUrl != null && artUrl.startsWith("https")) {
                        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(artUrl)
                        deleteTasks.add(storageRef.delete())
                    }

                    // Tache de suppression de la fiche Art
                    deleteTasks.add(db.collection("arts").document(document.id).delete())
                }

                // Tache de suppression du profil utilisateur
                deleteTasks.add(db.collection("users").document(uid).delete())

                // Execution des taches
                Tasks.whenAll(deleteTasks)
                    .addOnSuccessListener {
                        // Suppression compte
                        user.delete()
                            .addOnSuccessListener {
                                _isUserLoggedIn.value = false
                                onResult(true, null)
                            }
                            .addOnFailureListener { e ->
                                onResult(false, "Données supprimées, mais échec suppression compte : ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        onResult(false, "Erreur lors du nettoyage des données : ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, "Impossible de récupérer vos données : ${e.message}")
            }
    }

    // Reset de l'état d'authentification
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    // ===== CATEGORIES =====
    // Liste entière des catégories (tous y compris)
    private val allCategory = Category(0, "Tous")
    private val _categories = listOf(
        allCategory,
        Category(1, "Peinture"),
        Category(2, "Musique"),
        Category(3, "Écriture"),
        Category(4, "Dessin"),
        Category(5, "Jeux Vidéos"),
        Category(6, "Sculpture")
    )

    // Liste des catégories (sans "tous")
    val categoriesNoAll: List<Category> = _categories.filter { it.id != 0 }
    val categories: List<Category> = _categories

    // État de la catégorie sélectionnée
    private val _selectedCategory = MutableStateFlow<Category?>(allCategory)
    val selectedCategory = _selectedCategory.asStateFlow()


    // ===== ARTS =====

    // Récupère les oeuvres de Firestore
    private val _allArtsFromFirestore = MutableStateFlow<List<Art>>(emptyList())

    val allArts: StateFlow<List<Art>> = _allArtsFromFirestore.asStateFlow()

    // Liste des arts dans Firestore
    val arts: StateFlow<List<Art>> = _allArtsFromFirestore
        .combine(_selectedCategory) { allArts, selectedCat ->
            if (selectedCat == null || selectedCat.id == 0) {
                allArts
            } else {
                allArts.filter { it.category.id == selectedCat.id }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Maintient le Flow actif
            initialValue = emptyList()
        )

    init {
        // Ecoute en temps réel de Firestore à l'initialisation
        fetchArtsRealtime()
    }

    // Écoute en temps réel de Firestore
    private fun fetchArtsRealtime() {
        db.collection("arts")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MiArteViewModel", "Erreur écoute Firestore", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val artList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<Art>()?.copy(id = doc.id)
                    }
                    _allArtsFromFirestore.value = artList
                }
            }
    }

    // Liste des oeuvres de l'utilisateur authentifié
    private val _myArts = MutableStateFlow<List<Art>>(emptyList())
    val myArts: StateFlow<List<Art>> = _myArts.asStateFlow()

    fun fetchMyArts() {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId.isNullOrEmpty()) {
            _myArts.value = emptyList()
            return
        }

        db.collection("arts")
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val artsList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Art::class.java)?.copy(id = doc.id)
                    }
                    _myArts.value = artsList
                }
            }
    }

    // Sélection de Catégorie
    fun selectCategory(category: Category) {
        if (_selectedCategory.value?.id == category.id) {
            _selectedCategory.value = allCategory
        } else {
            _selectedCategory.value = category
        }
    }

    // Ajout d'une œuvre
    fun addArt(title: String, description: String, price: String, imageUri: Uri, category: Category) {
        val authorName = firebaseAuth.currentUser?.displayName ?: "Anonyme"
        val currentUserId = firebaseAuth.currentUser?.uid ?: return

        // Référence de l'image dans le storage
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        // Envoie du fichier vers Firebase
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                val newArt = Art(
                    id = "",
                    title = title,
                    imageUrl = downloadUrl.toString(),
                    userId = currentUserId,
                    author = authorName,
                    description = description,
                    price = price,
                    category = category
                )

                // Sauvegarde de l'art dans Firestore
                db.collection("arts")
                    .add(newArt)
                    .addOnSuccessListener { Log.d("MiArteViewModel", "Oeuvre ajoutée avec succès") }
                    .addOnFailureListener { e -> Log.w("MiArteViewModel", "Erreur ajout Firestore", e) }
            }
        }.addOnFailureListener { e ->
            Log.e("MiArteViewModel", "Erreur lors de l'upload de l'image", e)
        }
    }

    // Vérifie si l'oeuvre appartient à l'utilisateur
    fun isArtOwner(art: Art): Boolean {
        return firebaseAuth.currentUser?.uid == art.userId
    }

    // Fonction de suppression complète (Image + Base de données)
    fun deleteArt(art: Art, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // Si l'image existe et vient de Firebase, on la supprime d'abord
        if (art.imageUrl.startsWith("https://firebasestorage")) {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(art.imageUrl)
            storageRef.delete().addOnSuccessListener {
                // Si l'image est supprimée, on supprime le document Firestore
                deleteArtDocument(art.id, onSuccess, onError)
            }.addOnFailureListener {
                // Même si l'image plante, on essaye quand même de supprimer le document
                deleteArtDocument(art.id, onSuccess, onError)
            }
        } else {
            // Pas d'image Firebase, on supprime direct le document
            deleteArtDocument(art.id, onSuccess, onError)
        }
    }

    // Suppression du document d'arts (tout ce qui est autre que l'image)
    private fun deleteArtDocument(artId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        db.collection("arts").document(artId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Erreur inconnue") }
    }
}