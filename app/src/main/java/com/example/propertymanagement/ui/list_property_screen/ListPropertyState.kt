package com.example.propertymanagement.ui.list_property_screen

import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.model.Property


data class ListPropertyState(
    val isLoading: Boolean = false,
    val properties: List<Property> = emptyList(),
    val propertiesFilter: List<Property> = emptyList(),
    val activeFilters: FiltersProperty? = null,
    val error: String? = null,
    val currentUserId: String? = null,
    val searchQuery: String = "",

    val currencyRates: Map<String, CurrencyRate> = emptyMap()
)
