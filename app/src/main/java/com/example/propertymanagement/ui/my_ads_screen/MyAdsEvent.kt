package com.example.propertymanagement.ui.my_ads_screen

sealed class MyAdsEvent {
    object NavigateBack : MyAdsEvent()
    object ShowAuthRequired : MyAdsEvent()
}
