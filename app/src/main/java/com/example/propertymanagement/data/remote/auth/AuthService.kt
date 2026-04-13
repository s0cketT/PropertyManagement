package com.example.propertymanagement.data.remote.auth

import com.example.propertymanagement.data.model.UserDto
import com.example.propertymanagement.domain.model.SellerType

interface AuthService {

    suspend fun sendOtp(email: String)

    suspend fun verifyOtp(
        email: String,
        code: String
    ): UserDto

    suspend fun logout()

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        sellerType: SellerType
    )

    suspend fun signIn(email: String, password: String): UserDto

    suspend fun getCurrentUser(): UserDto?
}