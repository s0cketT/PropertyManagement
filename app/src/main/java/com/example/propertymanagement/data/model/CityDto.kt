package com.example.propertymanagement.data.model

data class CityDto(
    val id: Long,
    val region_id: Long,
    val name: String,
    val lat: Double?,
    val lng: Double?,
)

