package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.HeatingType
import com.example.propertymanagement.R

fun HeatingType.titleRes(): Int = when (this) {
    HeatingType.GAS -> R.string.heating_gas
    HeatingType.CENTRAL -> R.string.heating_central
    HeatingType.ELECTRIC -> R.string.heating_electric
    HeatingType.STOVE -> R.string.heating_stove
    HeatingType.NONE -> R.string.heating_none
}