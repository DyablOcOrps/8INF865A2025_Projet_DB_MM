package com.example.miarte.model

data class Art(
    val id: Int,                // ID de l'oeuvre
    val title: String,          // Titre
    val imageUrl: String,       // URL de l'image
    val author: String,         // Nom de l'Auteur
    val description: String,    // Description
    val price: String,          // Prix
    val category : Category     // Catégorie
)
