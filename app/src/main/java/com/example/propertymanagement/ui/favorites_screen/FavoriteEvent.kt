package com.example.propertymanagement.ui.favorites_screen

import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.ui.list_property_screen.ListPropertyEvent

sealed class FavoriteEvent {

    object NavigateBack : FavoriteEvent()

    data class NavigateToDetail(val property: Property, val userId: String) : FavoriteEvent()

    object ShowAuthRequired : FavoriteEvent()
}