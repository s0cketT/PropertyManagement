package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.CommercialRepairType
import com.example.propertymanagement.R

fun CommercialRepairType.titleRes(): Int = when (this) {
    CommercialRepairType.OFFICE_FINISH -> R.string.repair_office_finish
    CommercialRepairType.REQUIRES_CAPITAL_REPAIR -> R.string.repair_requires_capital
    CommercialRepairType.REQUIRES_COSMETIC_REPAIR -> R.string.repair_requires_cosmetic
}