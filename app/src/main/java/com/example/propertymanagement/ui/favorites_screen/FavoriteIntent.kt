package com.example.propertymanagement.ui.favorites_screen

import com.example.propertymanagement.domain.model.Property

sealed class FavoriteIntent {

    object NavigateBack : FavoriteIntent()

    object NavigateToLogin : FavoriteIntent()

    data class ToggleFavorite(val propertyId: Int) : FavoriteIntent()

    data class OnPropertyClick(val property: Property) : FavoriteIntent()

}