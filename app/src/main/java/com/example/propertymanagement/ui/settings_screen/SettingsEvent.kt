package com.example.propertymanagement.ui.settings_screen

sealed class SettingsEvent {
    object NavigateBack : SettingsEvent()

    object ChangeLanguage : SettingsEvent()

    object NavigateToAuth : SettingsEvent()

    object ApplyTheme : SettingsEvent()
}