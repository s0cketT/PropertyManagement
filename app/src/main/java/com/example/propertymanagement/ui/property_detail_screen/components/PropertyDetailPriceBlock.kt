package com.example.propertymanagement.ui.property_detail_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices
import com.example.propertymanagement.ui.common.PropertyMultiCurrencyPriceColumn

@Composable
fun PropertyDetailPriceBlock(
    property: Property,
    convertedPrices: PropertyDetailPrices?,
    modifier: Modifier = Modifier,
    managerCommissionPercent: Double = 0.0,
    showBuyerCommissionCaption: Boolean = true,
    /** `null` — валюта объявления первой («Мои объявления»). */
    cardPriceLeadCurrency: CurrencyType? = null,
) {
    PropertyMultiCurrencyPriceColumn(
        property = property,
        convertedPrices = convertedPrices,
        compact = false,
        modifier = modifier,
        primaryBold = true,
        managerCommissionPercent = managerCommissionPercent,
        showBuyerCommissionCaption = showBuyerCommissionCaption,
        priceLeadCurrency = cardPriceLeadCurrency,
    )
}
