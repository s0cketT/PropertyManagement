package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.BalconyType
import com.example.propertymanagement.R

fun BalconyType.titleRes(): Int = when (this) {
    BalconyType.NONE -> R.string.balcony_none
    BalconyType.BALCONY -> R.string.balcony_balcony
    BalconyType.LOGGIA -> R.string.balcony_loggia
    BalconyType.TWO_PLUS -> R.string.balcony_two_plus
}