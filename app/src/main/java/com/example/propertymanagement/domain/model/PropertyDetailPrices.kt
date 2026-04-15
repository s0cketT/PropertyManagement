package com.example.propertymanagement.domain.model

/**
 * Цена объекта, пересчитанная во все поддерживаемые валюты (для экрана деталей).
 */
data class PropertyDetailPrices(
    val usd: Double,
    val eur: Double,
    val byn: Double
)
