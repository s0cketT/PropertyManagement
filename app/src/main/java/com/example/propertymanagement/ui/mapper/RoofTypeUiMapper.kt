package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.RoofType
import com.example.propertymanagement.R

fun RoofType.titleRes(): Int = when (this) {
    RoofType.FLAT -> R.string.roof_flat
    RoofType.SINGLE_SLOPE -> R.string.roof_single_slope
    RoofType.DOUBLE_SLOPE -> R.string.roof_double_slope
    RoofType.OTHER -> R.string.roof_other
}