package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.currency.convertAmountBetweenCurrencies
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices
import com.example.propertymanagement.domain.pricing.ManagerCommissionPricing

class GetPropertyDetailPricesUseCase {

    operator fun invoke(
        property: Property,
        rates: Map<String, CurrencyRate>,
        managerCommissionPercent: Double = 0.0,
    ): PropertyDetailPrices? {
        if (rates[CurrencyType.USD.name] == null || rates[CurrencyType.EUR.name] == null) {
            return null
        }

        val from = property.currency
        val amount = ManagerCommissionPricing.grossListingAmount(
            listedPrice = property.price,
            commissionPercent = managerCommissionPercent,
        )

        return PropertyDetailPrices(
            usd = convertAmountBetweenCurrencies(amount, from, CurrencyType.USD, rates),
            eur = convertAmountBetweenCurrencies(amount, from, CurrencyType.EUR, rates),
            byn = convertAmountBetweenCurrencies(amount, from, CurrencyType.BYN, rates)
        )
    }
}
