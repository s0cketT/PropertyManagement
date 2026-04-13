package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.R

fun CommercialPropertyType.titleRes(): Int = when (this) {
    CommercialPropertyType.OFFICE -> R.string.commercial_type_office
    CommercialPropertyType.SHOP -> R.string.commercial_type_shop
    CommercialPropertyType.INDUSTRIAL -> R.string.commercial_type_industrial
    CommercialPropertyType.WAREHOUSE -> R.string.commercial_type_warehouse
    CommercialPropertyType.OTHER -> R.string.commercial_type_other
}