package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.ThemeType
import com.example.propertymanagement.R

fun ThemeType.titleRes(): Int = when (this) {
    ThemeType.SYSTEM -> R.string.system_theme
    ThemeType.DARK -> R.string.dark_theme
    ThemeType.LIGHT -> R.string.light_theme
}