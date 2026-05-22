package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.NearbyMapPoi

interface IOverpassPoiRepository {

    /**
     * Школы, поликлиники/амбулатории, продуктовые магазины в [radiusMeters] от точки (WGS84).
     * Источник: OpenStreetMap через Overpass API (один запрос).
     */
    suspend fun fetchMapPoisAround(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int,
    ): Result<List<NearbyMapPoi>>
}
