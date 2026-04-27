package com.example.propertymanagement.domain.model

data class City(
    val id: Long,
    val regionId: Long,
    val name: String,
    val lat: Double?,
    val lng: Double?,
)

