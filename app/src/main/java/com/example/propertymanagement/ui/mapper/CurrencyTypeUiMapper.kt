package com.example.propertymanagement.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyType

@Composable
fun CurrencyType.symbol(): String {
    return when (this) {
        CurrencyType.USD -> stringResource(R.string.currency_usd)
        CurrencyType.EUR -> stringResource(R.string.currency_eur)
        CurrencyType.BYN -> stringResource(R.string.currency_byn)
    }
}

fun CurrencyType.toCode(): String = when (this) {
    CurrencyType.USD -> "USD"
    CurrencyType.EUR -> "EUR"
    CurrencyType.BYN -> "BYN"
}