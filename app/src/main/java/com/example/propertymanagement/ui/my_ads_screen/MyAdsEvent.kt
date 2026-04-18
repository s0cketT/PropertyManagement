package com.example.propertymanagement.ui.my_ads_screen

import com.example.propertymanagement.domain.model.Property

sealed class MyAdsEvent {
    object NavigateBack : MyAdsEvent()
    data class NavigateToDetail(val property: Property, val userId: String) : MyAdsEvent()
    object ShowAuthRequired : MyAdsEvent()
}
