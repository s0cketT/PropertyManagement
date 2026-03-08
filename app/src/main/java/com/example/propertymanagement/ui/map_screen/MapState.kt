package com.example.propertymanagement.ui.map_screen

import com.example.propertymanagement.domain.model.PropertyMarker
import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.domain.model.UserLocation


data class MapState(
    val userLocation: UserLocation? = null,
    val markers: List<PropertyMarker> = emptyList(),
    val filteredMarkers: List<PropertyMarker> = emptyList(),
    val selectedStatuses: Set<PropertyStatus> = emptySet(),
    val selectedTypes: Set<PropertyType> = emptySet()
)
