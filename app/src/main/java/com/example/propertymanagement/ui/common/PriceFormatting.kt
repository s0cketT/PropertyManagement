package com.example.propertymanagement.ui.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

/**
 * Форматирует цену с разделителем групп разрядов (пробел), например 50 000, 100 000,12 345.67.
 */
fun formatPrice(price: Double): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }
    val whole = abs(price - price.toLong()) < 1e-6
    val pattern = if (whole) "#,##0" else "#,##0.00"
    return DecimalFormat(pattern, symbols).format(price)
}
