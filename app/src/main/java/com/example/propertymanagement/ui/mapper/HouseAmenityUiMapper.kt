package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.HouseAmenity
import com.example.propertymanagement.R

fun HouseAmenity.titleRes(): Int = when (this) {
    HouseAmenity.FIREPLACE -> R.string.house_amenity_fireplace
    HouseAmenity.FURNITURE -> R.string.house_amenity_furniture
    HouseAmenity.JACUZZI -> R.string.house_amenity_jacuzzi
    HouseAmenity.PARKING -> R.string.house_amenity_parking
    HouseAmenity.GARAGE -> R.string.house_amenity_garage
    HouseAmenity.SECURITY -> R.string.house_amenity_security
}