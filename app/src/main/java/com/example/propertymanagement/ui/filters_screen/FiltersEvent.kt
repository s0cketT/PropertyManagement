package com.example.propertymanagement.ui.filters_screen

sealed class FiltersEvent {

    data object NavigateBack : FiltersEvent()

    data object NavigateToCategorySelection : FiltersEvent()
}