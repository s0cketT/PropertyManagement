package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.ApartmentRepairType
import com.example.propertymanagement.R

fun ApartmentRepairType.titleRes(): Int = when (this) {
    ApartmentRepairType.COSMETIC -> R.string.repair_cosmetic
    ApartmentRepairType.EURO -> R.string.repair_euro
    ApartmentRepairType.DESIGNER -> R.string.repair_designer
    ApartmentRepairType.ROUGH_FINISH -> R.string.repair_rough
    ApartmentRepairType.NEEDS_REPAIR -> R.string.repair_needs
    ApartmentRepairType.EMERGENCY -> R.string.repair_emergency
}