package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.ThemeType
import kotlinx.coroutines.flow.Flow

interface IThemeRepository {
    suspend fun setTheme(theme: ThemeType)
    fun observeTheme(): Flow<ThemeType>
}