package com.example.propertymanagement.ui.extensions

import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.R

fun DealType?.priceTitle(): Int {
    return when (this) {
        DealType.RENT -> R.string.price_per_month
        else -> R.string.price
    }
}