package com.example.propertymanagement.domain.currency

import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.CurrencyType

/**
 * Конвертация суммы между валютами по курсам НБ РБ (база — BYN).
 * [CurrencyRate.ratePerUnit] — сколько BYN за 1 единицу иностранной валюты (после учёта [CurrencyRate.scale]).
 */
fun convertAmountBetweenCurrencies(
    amount: Double,
    from: CurrencyType,
    to: CurrencyType,
    rates: Map<String, CurrencyRate>
): Double {
    if (from == to) return amount
    val inByn = amountToByn(amount, from, rates)
    return fromBynToCurrency(inByn, to, rates)
}

private fun amountToByn(amount: Double, currency: CurrencyType, rates: Map<String, CurrencyRate>): Double {
    if (currency == CurrencyType.BYN) return amount
    val rate = rates[currency.name]?.ratePerUnit ?: return amount
    return amount * rate
}

private fun fromBynToCurrency(byn: Double, currency: CurrencyType, rates: Map<String, CurrencyRate>): Double {
    if (currency == CurrencyType.BYN) return byn
    val rate = rates[currency.name]?.ratePerUnit ?: 1.0
    return byn / rate
}
