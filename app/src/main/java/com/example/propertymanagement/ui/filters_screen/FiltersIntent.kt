package com.example.propertymanagement.ui.filters_screen

sealed class FiltersIntent {
    data object NavigateBack : FiltersIntent()

    data object NavigateToCategorySelection : FiltersIntent()
}