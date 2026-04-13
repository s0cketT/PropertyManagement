package com.example.propertymanagement.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.propertymanagement.domain.model.ThemeType
import com.example.propertymanagement.domain.repository.IThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : IThemeRepository {

    private val KEY = stringPreferencesKey("app_theme")

    override suspend fun setTheme(theme: ThemeType) {
        dataStore.edit {
            it[KEY] = theme.name
        }
    }

    override fun observeTheme(): Flow<ThemeType> {
        return dataStore.data.map { prefs ->
            prefs[KEY]?.let { ThemeType.valueOf(it) }
                ?: ThemeType.SYSTEM
        }
    }
}