package com.example.propertymanagement.domain.model

/**
 * Точка интереса из OSM рядом с объектом недвижимости.
 */
data class NearbyMapPoi(
    val osmId: Long,
    val osmType: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val category: NearbyPoiCategory,
)
