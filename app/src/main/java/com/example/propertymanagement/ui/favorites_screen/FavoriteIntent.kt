package com.example.propertymanagement.ui.favorites_screen

import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.ui.list_property_screen.ListPropertyIntent
import com.example.propertymanagement.ui.publish_screen.PublishIntent

sealed class FavoriteIntent {

    object NavigateBack : FavoriteIntent()

    data class ToggleFavorite(val propertyId: Int) : FavoriteIntent()

    data class OnPropertyClick(val property: Property) : FavoriteIntent()

}