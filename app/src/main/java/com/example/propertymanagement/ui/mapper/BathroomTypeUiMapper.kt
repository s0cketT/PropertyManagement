package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.BathroomType
import com.example.propertymanagement.R

fun BathroomType.titleRes(): Int = when (this) {
    BathroomType.SEPARATE -> R.string.bathroom_separate
    BathroomType.COMBINED -> R.string.bathroom_combined
    BathroomType.TWO -> R.string.bathroom_two
    BathroomType.THREE_PLUS -> R.string.bathroom_three_plus
}