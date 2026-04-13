package com.example.propertymanagement.ui.core.locale

import com.example.propertymanagement.domain.model.LanguageType
import java.util.Locale

object LocaleManager {

    fun setLocale(language: LanguageType) {
        val locale = when (language) {
            LanguageType.RU -> Locale("ru")
            LanguageType.EN -> Locale("en")
        }

        Locale.setDefault(locale)
    }
}