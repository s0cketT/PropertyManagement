package com.example.propertymanagement.ui.map_screen

import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.PropertyType

sealed class MapIntent {

    data object LoadUserLocation : MapIntent()

    data object NavigateBack : MapIntent()
    data object NavigateFilterScreen : MapIntent()

    data class ApplyStatusFilter(val status: PropertyStatus?) : MapIntent()

    data class ApplyTypeFilter(val type: PropertyType?) : MapIntent()

    data class MarkerTapped(val property: Property) : MapIntent()

    data object DismissMarkerBottomSheet : MapIntent()

    data object NavigateToSelectedPropertyDetail : MapIntent()
}