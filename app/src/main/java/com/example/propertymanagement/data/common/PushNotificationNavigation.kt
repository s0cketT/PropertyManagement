package com.example.propertymanagement.data.common

import com.example.propertymanagement.domain.model.MyAdsListingFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PushNotificationNavigation {

    private val pendingLock = Any()
    private val _pendingMyAdsTab = MutableStateFlow<MyAdsListingFilter?>(null)
    val pendingMyAdsTab: StateFlow<MyAdsListingFilter?> = _pendingMyAdsTab.asStateFlow()

    fun requestOpenMyAdsFromPush(listingFilter: MyAdsListingFilter) {
        synchronized(pendingLock) {
            _pendingMyAdsTab.value = listingFilter
        }
    }

    fun clearPendingOpenMyAds() {
        synchronized(pendingLock) {
            _pendingMyAdsTab.value = null
        }
    }

    fun takePendingMyAdsTab(): MyAdsListingFilter? {
        synchronized(pendingLock) {
            val tab = _pendingMyAdsTab.value ?: return null
            _pendingMyAdsTab.value = null
            return tab
        }
    }
}
