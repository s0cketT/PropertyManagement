package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.ThemeType
import com.example.propertymanagement.domain.repository.IThemeRepository
import kotlinx.coroutines.flow.Flow

class ObserveThemeUseCase(
    private val themeRepository: IThemeRepository
) {
    operator fun invoke(): Flow<ThemeType> {
        return themeRepository.observeTheme()
    }
}