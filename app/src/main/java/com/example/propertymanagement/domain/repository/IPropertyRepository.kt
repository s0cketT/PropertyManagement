package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.PropertyMarker

interface IPropertyRepository {

    suspend fun getMarkers(): List<PropertyMarker>

}