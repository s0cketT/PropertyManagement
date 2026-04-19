package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.AuthRepository

class UpdatePasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newPassword: String) {
        authRepository.updatePassword(newPassword)
    }
}
