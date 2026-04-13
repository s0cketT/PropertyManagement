package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.R

fun PropertyType.titleRes(): Int = when (this) {
    PropertyType.APARTMENT -> R.string.category_apartments
    PropertyType.HOUSE -> R.string.category_houses
    PropertyType.COMMERCIAL -> R.string.category_commercial
    PropertyType.GARAGE -> R.string.category_garages
    PropertyType.ROOM -> R.string.category_rooms
}

fun PropertyType.descriptionRes(): Int = when (this) {
    PropertyType.APARTMENT -> R.string.description_apartments
    PropertyType.HOUSE -> R.string.description_houses
    PropertyType.COMMERCIAL -> R.string.description_commercial
    PropertyType.GARAGE -> R.string.description_garages
    PropertyType.ROOM -> R.string.description_rooms
}