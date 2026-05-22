package com.example.propertymanagement.ui.map_screen

sealed class MapEvent {

    data object NavigateBack : MapEvent()
    data object NavigateFilterScreen : MapEvent()

    data object ShowAuthRequiredForFavoritesOnly : MapEvent()

    data class NavigateToPropertyDetail(val propertyId: Int, val userId: String) : MapEvent()
}