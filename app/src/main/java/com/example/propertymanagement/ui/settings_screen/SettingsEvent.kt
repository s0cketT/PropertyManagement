package com.example.propertymanagement.ui.settings_screen

sealed class SettingsEvent {
    object NavigateBack : SettingsEvent()

    object ChangeLanguage : SettingsEvent()

    object NavigateToAuth : SettingsEvent()

    object ApplyTheme : SettingsEvent()

    data class NavigateToPasswordResetOtp(val email: String) : SettingsEvent()

    object ChangePasswordSendFailed : SettingsEvent()

    data class NavigateToChangeEmailOtpOld(val email: String) : SettingsEvent()

    object ChangeEmailSendFailed : SettingsEvent()
}