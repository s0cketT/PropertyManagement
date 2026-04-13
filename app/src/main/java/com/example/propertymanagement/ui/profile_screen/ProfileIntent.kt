package com.example.propertymanagement.ui.profile_screen

sealed class ProfileIntent {
    object LoginClick : ProfileIntent()
    object NavToPublish : ProfileIntent()
    object MyAds : ProfileIntent()
    object RateApp : ProfileIntent()
    object Settings : ProfileIntent()

    object PersonalInfo : ProfileIntent()
}