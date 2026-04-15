package com.example.propertymanagement.ui.property_detail_screen.components

import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetails
import java.util.Locale

internal fun effectiveArea(property: Property): Double? {
    val details = property.details
    return when (details) {
        is PropertyDetails.Apartment -> details.livingArea ?: property.area
        is PropertyDetails.Room -> details.saleArea ?: property.area
        is PropertyDetails.House -> details.landArea ?: property.area
        else -> property.area
    }
}

internal fun formatDouble(value: Double): String =
    if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", value)
    }

