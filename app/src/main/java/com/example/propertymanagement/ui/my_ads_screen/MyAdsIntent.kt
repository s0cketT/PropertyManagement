package com.example.propertymanagement.ui.my_ads_screen

import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.example.propertymanagement.domain.model.Property

sealed class MyAdsIntent {
    object NavigateBack : MyAdsIntent()
    object DismissPropertyDetailSheet : MyAdsIntent()
    object DismissDeleteDialog : MyAdsIntent()
    object ConfirmDeleteProperty : MyAdsIntent()
    data class SelectListingFilter(val filter: MyAdsListingFilter) : MyAdsIntent()
    data class OnPropertyClick(val property: Property) : MyAdsIntent()
    data class RequestDeleteProperty(val property: Property) : MyAdsIntent()
}
