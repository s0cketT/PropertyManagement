package com.example.propertymanagement.ui.extensions

import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.ui.filters_screen.FiltersState
import com.example.propertymanagement.domain.model.StringRangeFilter


fun FiltersState.clearPricePerMeter(): FiltersState {
    return copy(
        pricePerMeter = StringRangeFilter(),
        selectedCurrency = CurrencyType.USD
    )
}

fun FiltersState.clearPrice(): FiltersState {
    return copy(
        price = StringRangeFilter(),
        selectedCurrency = CurrencyType.USD
    )
}