package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.LanguageType
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    suspend fun setLanguage(language: LanguageType)
    fun observeLanguage(): Flow<LanguageType>
}