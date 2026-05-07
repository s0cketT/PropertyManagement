package com.example.propertymanagement.ui.bottom_nav

import androidx.navigation.NavController
import com.example.propertymanagement.data.common.PushNotificationNavigation
import com.example.propertymanagement.domain.model.MyAdsListingFilter

fun NavController.resetStackToAdvertisementsThenMyAds(initialTab: MyAdsListingFilter) {
    navigate(Screens.Advertisements.route) {
        popUpTo(0) { inclusive = true }
    }
    navigate(Screens.MyAdsScreen.createRoute(initialTab)) {
        launchSingleTop = true
    }
}

fun NavController.navigateToMainOrMyAdsFromModerationPush() {
    val tab = PushNotificationNavigation.takePendingMyAdsTab()
    if (tab != null) {
        resetStackToAdvertisementsThenMyAds(tab)
    } else {
        navigate(Screens.Advertisements.route) {
            popUpTo(0) { inclusive = true }
        }
    }
}
