package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.WaterType
import com.example.propertymanagement.R

fun WaterType.titleRes(): Int = when (this) {
    WaterType.WELL -> R.string.water_well
    WaterType.WELL_SHAFT -> R.string.water_well_shaft
    WaterType.CENTRAL -> R.string.water_central
    WaterType.NONE -> R.string.water_none
}