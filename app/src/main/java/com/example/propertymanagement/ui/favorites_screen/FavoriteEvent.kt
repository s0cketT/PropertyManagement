package com.example.propertymanagement.ui.favorites_screen

import com.example.propertymanagement.domain.model.Property

sealed class FavoriteEvent {

    object NavigateBack : FavoriteEvent()

    object NavigateToLogin : FavoriteEvent()

    data class NavigateToDetail(val property: Property, val userId: String) : FavoriteEvent()

    object ShowAuthRequired : FavoriteEvent()
}