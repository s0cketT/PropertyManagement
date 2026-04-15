package com.example.propertymanagement.domain.model

data class GeocodedAddressParts(
    val country: String = "",
    val region: String = "",
    val city: String = "",
    val street: String = "",
    val house: String = ""
)
