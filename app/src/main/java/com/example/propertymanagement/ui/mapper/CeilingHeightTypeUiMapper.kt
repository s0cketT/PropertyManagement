package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.CeilingHeightType
import com.example.propertymanagement.R

fun CeilingHeightType.titleRes(): Int = when (this) {
    CeilingHeightType.H_2_5 -> R.string.ceiling_2_5
    CeilingHeightType.H_2_7 -> R.string.ceiling_2_7
    CeilingHeightType.H_3_0 -> R.string.ceiling_3_0
    CeilingHeightType.H_3_5 -> R.string.ceiling_3_5
    CeilingHeightType.H_4_0 -> R.string.ceiling_4_0
}