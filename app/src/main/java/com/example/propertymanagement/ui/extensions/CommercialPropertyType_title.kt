package com.example.propertymanagement.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.R

@Composable
fun CommercialPropertyType.title(): String {
    return when (this) {
        CommercialPropertyType.OFFICE -> stringResource(R.string.commercial_type_office)
        CommercialPropertyType.SHOP -> stringResource(R.string.commercial_type_shop)
        CommercialPropertyType.INDUSTRIAL -> stringResource(R.string.commercial_type_industrial)
        CommercialPropertyType.WAREHOUSE -> stringResource(R.string.commercial_type_warehouse)
        CommercialPropertyType.OTHER -> stringResource(R.string.commercial_type_other)
    }
}