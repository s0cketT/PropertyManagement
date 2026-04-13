package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.ThemeType
import com.example.propertymanagement.domain.repository.IThemeRepository

class SetThemeUseCase(
    private val themeRepository: IThemeRepository
) {
    suspend operator fun invoke(theme: ThemeType) {
        themeRepository.setTheme(theme)
    }
}