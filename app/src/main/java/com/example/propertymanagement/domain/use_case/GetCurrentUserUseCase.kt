package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.AuthUser
import com.example.propertymanagement.domain.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AuthUser? {
        return authRepository.getCurrentUser()
    }
}