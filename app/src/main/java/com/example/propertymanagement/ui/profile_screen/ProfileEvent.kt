package com.example.propertymanagement.ui.profile_screen

sealed class ProfileEvent {
    object NavigateToAuth : ProfileEvent()
    object NavigateToPublishScreen : ProfileEvent()
    object NavigateToMyAds : ProfileEvent()
    object NavigateToMyApplications : ProfileEvent()
    object NavigateToSettings : ProfileEvent()

    object NavigateToPersonalInfo : ProfileEvent()

    object NavigateBack : ProfileEvent()

    object AppRatingSaved : ProfileEvent()

    object AppRatingSaveFailed : ProfileEvent()
}