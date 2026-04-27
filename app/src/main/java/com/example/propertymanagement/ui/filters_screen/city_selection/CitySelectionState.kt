package com.example.propertymanagement.ui.filters_screen.city_selection

import com.example.propertymanagement.domain.model.City

data class CitySelectionState(
    val isLoading: Boolean = true,
    val cities: List<City> = emptyList(),
    val selectedCityIds: Set<Long> = emptySet(),
    val error: Throwable? = null,
)

