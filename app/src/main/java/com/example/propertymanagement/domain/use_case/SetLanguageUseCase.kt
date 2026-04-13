package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.domain.repository.ISettingsRepository

class SetLanguageUseCase(
    private val settingsRepository: ISettingsRepository
) {
    suspend operator fun invoke(language: LanguageType) {
        settingsRepository.setLanguage(language)
    }
}