package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.ParkingType
import com.example.propertymanagement.R

fun ParkingType.titleRes(): Int = when (this) {
    ParkingType.ROOF -> R.string.parking_roof
    ParkingType.UNDERGROUND -> R.string.parking_underground
    ParkingType.GROUND -> R.string.parking_ground
    ParkingType.OPEN -> R.string.parking_open
}