package com.example.propertymanagement.domain.pricing

import java.math.BigDecimal

/** Строка для UI: целые без «.0», иначе до 2 знаков без лишних нулей. */
fun formatCommissionPercentForDisplay(value: Double): String {
    if (value.isNaN() || value.isInfinite()) return "0"
    val bd = BigDecimal.valueOf(value).stripTrailingZeros()
    return bd.toPlainString()
}
