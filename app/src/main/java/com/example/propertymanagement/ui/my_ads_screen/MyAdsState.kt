package com.example.propertymanagement.ui.my_ads_screen

import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.example.propertymanagement.domain.model.Property

data class MyAdsState(
    val isLoading: Boolean = false,
    val properties: List<Property> = emptyList(),
    val visibleList: List<Property> = emptyList(),
    val listingFilter: MyAdsListingFilter = MyAdsListingFilter.ALL,
    val error: String? = null,
    val currencyRates: Map<String, CurrencyRate> = emptyMap(),
    val currentUserId: String? = null
)
