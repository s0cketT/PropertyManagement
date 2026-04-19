package com.example.propertymanagement.ui.settings_screen

import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.domain.model.ThemeType

data class SettingsState(
    val selectedTheme: ThemeType = ThemeType.SYSTEM,
    val selectedLanguage: LanguageType = LanguageType.RU,
    val showLanguageSheet: Boolean = false,
    val showChangePasswordSheet: Boolean = false,
    val isSendingChangePasswordCode: Boolean = false,
    val showChangeEmailSheet: Boolean = false,
    val isSendingChangeEmailCode: Boolean = false
)
