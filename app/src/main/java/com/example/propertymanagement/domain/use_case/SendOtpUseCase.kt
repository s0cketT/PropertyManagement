package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.AuthRepository

class SendOtpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) {
        authRepository.sendOtp(email)
    }
}