package com.example.propertymanagement.ui.list_property_screen

import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.FiltersProperty


fun catalogCardPriceLeadCurrency(filters: FiltersProperty?): CurrencyType {
    if (filters == null) {
        return CurrencyType.USD
    }

    return filters.selectedCurrency
}
