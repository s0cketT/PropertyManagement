package com.example.propertymanagement.ui.filters_screen

import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.PropertyType

data class FiltersState(
    val selectedStatuses: Set<PropertyStatus> = emptySet(),
    val selectedTypes: Set<PropertyType> = emptySet(),
)
