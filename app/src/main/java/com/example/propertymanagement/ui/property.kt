package com.example.propertymanagement.ui

enum class PropertyType {
    SALE,
    RENT
}

data class Property(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val type: PropertyType
)
