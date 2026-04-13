package com.example.propertymanagement.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.R

@Composable
fun SellerType.asString(): String {
    return when (this) {
        SellerType.OWNER -> stringResource(R.string.filter_seller_owner)
        SellerType.AGENT -> stringResource(R.string.filter_seller_agent)
    }
}