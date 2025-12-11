package com.example.miarte.model

data class Art(
    val id: String = "",                    // ID de l'art
    val title: String = "",                 // Titre de l'art
    val imageUrl: String = "",              // URL de l'image de l'art
    val author: String = "",                // Auteur de l'art
    val description: String = "",           // Description de l'art
    val price: String = "",                 // Prix de l'art
    val userId: String = "",                // ID de l'utilisateur propriétaire de l'art
    val category: Category = Category()     // Catégorie de l'art
)
