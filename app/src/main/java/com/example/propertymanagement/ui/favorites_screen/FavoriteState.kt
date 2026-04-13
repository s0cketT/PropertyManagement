package com.example.propertymanagement.ui.favorites_screen

import com.example.propertymanagement.domain.model.Property

data class FavoriteState(
    val isLoading: Boolean = false,
    val properties: List<Property> = emptyList(),
    val error: String? = null,
    val currentUserId: String? = null,
)
