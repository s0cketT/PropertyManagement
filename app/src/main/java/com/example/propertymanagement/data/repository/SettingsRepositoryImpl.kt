package com.example.propertymanagement.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.propertymanagement.domain.model.LanguageType
import com.example.propertymanagement.domain.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ISettingsRepository {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
    }

    override suspend fun setLanguage(language: LanguageType) {
        dataStore.edit {
            it[Keys.LANGUAGE] = language.name
        }
    }

    override fun observeLanguage(): Flow<LanguageType> {
        return dataStore.data.map {
            val value = it[Keys.LANGUAGE] ?: LanguageType.RU.name
            LanguageType.valueOf(value)
        }
    }
}