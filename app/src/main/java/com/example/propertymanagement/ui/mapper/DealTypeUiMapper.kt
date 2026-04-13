package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.R

fun DealType.titleResDefault(): Int = when (this) {
    DealType.BUY -> R.string.buy
    DealType.RENT -> R.string.rent
}

fun DealType.titleResPublish(): Int = when (this) {
    DealType.BUY -> R.string.sell
    DealType.RENT -> R.string.rent_out
}