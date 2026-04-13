package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.R

fun LanguageType.titleRes(): Int = when (this) {
    LanguageType.RU -> R.string.language_russian
    LanguageType.EN -> R.string.language_english
}