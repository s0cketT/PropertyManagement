package com.example.propertymanagement.ui.mapper

import androidx.annotation.StringRes
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.MyAdsListingFilter

@StringRes
fun MyAdsListingFilter.titleRes(): Int = when (this) {
    MyAdsListingFilter.ALL -> R.string.my_ads_filter_all
    MyAdsListingFilter.PUBLISHED -> R.string.my_ads_filter_published
    MyAdsListingFilter.PENDING -> R.string.my_ads_filter_moderation
    MyAdsListingFilter.REJECTED -> R.string.my_ads_filter_rejected
}
