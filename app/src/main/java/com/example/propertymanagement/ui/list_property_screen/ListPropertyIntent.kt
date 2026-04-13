package com.example.propertymanagement.ui.list_property_screen

import com.example.propertymanagement.domain.model.Property


sealed class ListPropertyIntent {
    object LoadProperties : ListPropertyIntent()

    data class ToggleFavorite(val propertyId: Int) : ListPropertyIntent()

    data class OnSearchChanged(val query: String) : ListPropertyIntent()

    data class OnPropertyClick(val property: Property) : ListPropertyIntent()

    object OnFilterClick : ListPropertyIntent()
}