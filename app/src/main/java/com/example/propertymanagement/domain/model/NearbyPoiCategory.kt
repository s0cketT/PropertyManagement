package com.example.propertymanagement.domain.model

/**
 * Категории объектов карты рядом с объявлением (данные OpenStreetMap / Overpass).
 */
enum class NearbyPoiCategory {
    SCHOOL,
    POLYCLINIC,
    GROCERY;

    companion object {
        val allAsSet: Set<NearbyPoiCategory> =
            entries.toSet()
    }
}
