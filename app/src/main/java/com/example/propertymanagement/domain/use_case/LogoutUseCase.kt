package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}