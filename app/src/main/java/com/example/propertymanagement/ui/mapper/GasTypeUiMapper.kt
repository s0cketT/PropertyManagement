package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.GasType
import com.example.propertymanagement.R

fun GasType.titleRes(): Int = when (this) {
    GasType.IN_HOUSE -> R.string.gas_in_house
    GasType.NEARBY -> R.string.gas_nearby
    GasType.NONE -> R.string.gas_none
}