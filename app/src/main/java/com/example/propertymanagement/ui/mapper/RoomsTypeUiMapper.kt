package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.RoomsType
import com.example.propertymanagement.R

fun RoomsType.titleRes(): Int = when (this) {
    RoomsType.ONE -> R.string.rooms_1
    RoomsType.TWO -> R.string.rooms_2
    RoomsType.THREE -> R.string.rooms_3
    RoomsType.FOUR -> R.string.rooms_4
    RoomsType.FIVE -> R.string.rooms_5
    RoomsType.FIVE_PLUS -> R.string.rooms_5_plus
}