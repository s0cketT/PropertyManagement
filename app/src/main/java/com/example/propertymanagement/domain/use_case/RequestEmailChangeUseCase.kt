package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.AuthRepository

class RequestEmailChangeUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(newEmail: String) {
        authRepository.requestEmailChange(newEmail)
    }
}
