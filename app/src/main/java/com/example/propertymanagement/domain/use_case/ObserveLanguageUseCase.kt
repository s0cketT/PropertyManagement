package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.domain.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveLanguageUseCase(
    private val settingsRepository: ISettingsRepository
) {
    operator fun invoke(): Flow<LanguageType> {
        return settingsRepository.observeLanguage()
    }
}