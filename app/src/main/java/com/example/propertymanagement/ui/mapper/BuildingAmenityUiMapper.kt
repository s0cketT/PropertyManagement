package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.BuildingAmenity
import com.example.propertymanagement.R

fun BuildingAmenity.titleRes(): Int = when (this) {
    BuildingAmenity.ELEVATOR -> R.string.amenity_elevator
    BuildingAmenity.TRASH_CHUTE -> R.string.amenity_trash_chute
    BuildingAmenity.GATED_AREA -> R.string.amenity_gated_area
    BuildingAmenity.INTERCOM -> R.string.amenity_intercom
    BuildingAmenity.BASEMENT -> R.string.amenity_basement
}