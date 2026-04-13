package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.HouseType
import com.example.propertymanagement.R

fun HouseType.titleRes(): Int = when (this) {
    HouseType.HOUSE -> R.string.house_type_house
    HouseType.COTTAGE -> R.string.house_type_cottage
    HouseType.DACHA -> R.string.house_type_dacha
    HouseType.OTHER -> R.string.house_type_other
}