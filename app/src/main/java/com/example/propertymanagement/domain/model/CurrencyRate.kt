package com.example.propertymanagement.domain.model

data class CurrencyRate(
    val code: String,
    val name: String,
    val scale: Int,
    val ratePerUnit: Double,
    val updatedAt: String? = null
) {
    companion object {
        val BYN = CurrencyRate(
            code = "BYN",
            name = "Белорусский рубль",
            scale = 1,
            ratePerUnit = 1.0
        )
    }
}
