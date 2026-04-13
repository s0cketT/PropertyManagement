package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.AuthUser
import com.example.propertymanagement.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): AuthUser {
        return authRepository.signIn(email, password)
    }
}