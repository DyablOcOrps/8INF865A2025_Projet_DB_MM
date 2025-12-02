package com.example.miarte.model

data class Art(
    val id: String = "",          // Changement Int -> String + valeur par défaut
    val title: String = "",
    val imageUrl: String = "",
    val author: String = "",
    val description: String = "",
    val price: String = "",
    val userId: String = "",
    val category: Category = Category() // Valeur par défaut nécessaire
)
