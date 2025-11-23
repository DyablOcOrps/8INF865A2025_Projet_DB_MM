package com.example.miarte.model

data class Art(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val author: String,
    val description: String,
    val price: String,
    val category : Category
)
