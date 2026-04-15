package com.example.propertymanagement.ui.list_property_screen

import com.example.propertymanagement.domain.model.Property

sealed class ListPropertyEvent {
    object ShowAuthRequired : ListPropertyEvent()

    data class NavigateToDetail(val property: Property, val userId: String) : ListPropertyEvent()

    object NavigateToFilterScreen : ListPropertyEvent()

    object NavigateBack : ListPropertyEvent()
}