package com.example.propertymanagement.ui.map_screen

import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.domain.model.PropertyType

sealed class MapIntent {

    data object LoadUserLocation : MapIntent()

    data object NavigateBack : MapIntent()
    data object NavigateFilterScreen : MapIntent()

    data class ApplyStatusFilter(val status: PropertyStatus?) : MapIntent()

    data class ApplyTypeFilter(val type: PropertyType?) : MapIntent()

}