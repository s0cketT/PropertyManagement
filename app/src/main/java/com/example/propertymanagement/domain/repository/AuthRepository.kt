package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.AuthUser
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendOtp(email: String)
    suspend fun verifyOtp(
        email: String,
        code: String
    ): User
    suspend fun logout()
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        sellerType: SellerType
    )
    suspend fun signIn(email: String, password: String): AuthUser
    suspend fun getCurrentUser(): AuthUser?

}
