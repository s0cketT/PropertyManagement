package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CommercialAmenity

fun CommercialAmenity.titleRes(): Int = when (this) {
    CommercialAmenity.FINISHING -> R.string.amenity_finishing
    CommercialAmenity.PRIVATE_BATHROOM -> R.string.amenity_private_bathroom
    CommercialAmenity.HOT_WATER -> R.string.amenity_hot_water
    CommercialAmenity.COLD_WATER -> R.string.amenity_cold_water
    CommercialAmenity.HEATING -> R.string.amenity_heating
    CommercialAmenity.SEPARATE_ENTRANCE -> R.string.amenity_separate_entrance
}