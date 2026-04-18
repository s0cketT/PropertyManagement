package com.example.propertymanagement.ui.my_ads_screen

import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.example.propertymanagement.domain.model.Property

sealed class MyAdsIntent {
    object NavigateBack : MyAdsIntent()
    data class SelectListingFilter(val filter: MyAdsListingFilter) : MyAdsIntent()
    data class OnPropertyClick(val property: Property) : MyAdsIntent()
    data class ToggleFavorite(val propertyId: Int) : MyAdsIntent()
}
