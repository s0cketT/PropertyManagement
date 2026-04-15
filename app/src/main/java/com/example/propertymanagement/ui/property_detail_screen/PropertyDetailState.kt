package com.example.propertymanagement.ui.property_detail_screen

import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices

data class PropertyDetailState(
    val isLoading: Boolean = true,
    val property: Property? = null,
    val error: String? = null,
    val notFound: Boolean = false,
    val isMapFullscreen: Boolean = false,
    val isImageViewerOpen: Boolean = false,
    val imageViewerInitialPage: Int = 0,
    val convertedPrices: PropertyDetailPrices? = null
)
