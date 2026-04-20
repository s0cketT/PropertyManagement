package com.example.propertymanagement.ui.edit_property_screen

fun EditPropertyState.buildAddressQueryString(): String {
    return listOf(
        addressCountry.trim(),
        addressRegion.trim(),
        addressCity.trim(),
        addressStreet.trim(),
        addressHouse.trim(),
    )
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}
