package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        sellerType: SellerType
    ) {
        authRepository.signUp(
            email = email,
            password = password,
            firstName = firstName,
            sellerType = sellerType
        )
    }
}