package com.example.propertymanagement.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.R

@Composable
fun CurrencyType.symbol(): String {
    return when (this) {
        CurrencyType.USD -> stringResource(R.string.currency_usd)
        CurrencyType.EUR -> stringResource(R.string.currency_eur)
        CurrencyType.BYN -> stringResource(R.string.currency_byn)
    }
}