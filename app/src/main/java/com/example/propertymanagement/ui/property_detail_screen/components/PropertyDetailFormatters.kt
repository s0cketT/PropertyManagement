package com.example.propertymanagement.ui.property_detail_screen.components

import com.example.propertymanagement.domain.model.Property
import java.util.Locale

internal fun effectiveArea(property: Property): Double? {
    return property.area
}

internal fun formatDouble(value: Double): String =
    if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", value)
    }

internal fun buildPropertyDetailAddressLine(property: Property): String =
    sequenceOf(property.country, property.region, property.city, property.street, property.house)
        .mapNotNull { part -> part?.trim()?.takeIf { it.isNotEmpty() } }
        .joinToString(", ")
