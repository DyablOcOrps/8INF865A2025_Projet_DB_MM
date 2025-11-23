package com.example.miarte.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.miarte.model.Art
import com.example.miarte.model.Category

class MiArteViewModel : ViewModel() {

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
    // Catégories visibles pour AddArtScreen
    val categoriesNoAll: List<Category> = _categories.filter { it.id != 0 }

    // Catégories visibles pour HomeScreen
    val categories: List<Category> = _categories

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    // ===== ARTS =====

    private val fullArtList = mutableListOf(
        Art(1, "Peinture Rouge", "", "Alice", "Une peinture rouge", "100", _categories[1]),
        Art(2, "Jazz Studio", "", "Bob", "Album jazz", "70", _categories[2]),
        Art(3, "Roman court", "", "Claire", "Petite histoire", "15", _categories[3])
    )

    private val _arts = MutableStateFlow<List<Art>>(fullArtList)
    val arts: StateFlow<List<Art>> = _arts.asStateFlow()

    // ===== FILTRAGE =====

    fun selectCategory(category: Category) {
        _selectedCategory.value = category

        _arts.value =
            if (category.id == 0) fullArtList
            else fullArtList.filter { it.category.id == category.id }
    }

    // ===== AJOUT D’UNE ŒUVRE =====

    fun addArt(title: String, description: String, price: String, imageUrl: String, category: Category) {
        val newId = (fullArtList.maxOfOrNull { it.id } ?: 0) + 1

        val newArt = Art(
            id = newId,
            title = title,
            imageUrl = imageUrl,
            author = "Utilisateur",
            description = description,
            price = price,
            category = category
        )

        fullArtList.add(newArt)
        _arts.value = fullArtList.toList()
    }

    fun getArtById(id: Int): Art? {
        return fullArtList.firstOrNull { it.id == id }
    }
}
