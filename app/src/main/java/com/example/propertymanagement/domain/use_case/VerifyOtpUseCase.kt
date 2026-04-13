package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.User
import com.example.propertymanagement.domain.repository.AuthRepository

class VerifyOtpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String): User {
        return authRepository.verifyOtp(email = email, code = code)
    }
}