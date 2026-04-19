package com.example.propertymanagement.ui.settings_screen

import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.domain.model.ThemeType

sealed class SettingsIntent {
    data class ChangeTheme(val theme: ThemeType) : SettingsIntent()
    data class ToggleLanguageSheet(val isVisible: Boolean) : SettingsIntent()
    data class SelectLanguage(val language: LanguageType) : SettingsIntent()

    object NavigateBack : SettingsIntent()

    object Save : SettingsIntent()

    object Logout : SettingsIntent()

    object ChangePassword : SettingsIntent()

    object DismissChangePasswordSheet : SettingsIntent()

    object ConfirmChangePasswordSendCode : SettingsIntent()

    object ChangeEmail : SettingsIntent()

    object DismissChangeEmailSheet : SettingsIntent()

    object ConfirmChangeEmailSendOldOtp : SettingsIntent()
}