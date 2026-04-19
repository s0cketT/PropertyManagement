package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.mapper.toAuthUser
import com.example.propertymanagement.data.mapper.toDomain
import com.example.propertymanagement.data.remote.auth.AuthService
import com.example.propertymanagement.domain.model.AuthUser
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.domain.model.User
import com.example.propertymanagement.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val service: AuthService
) : AuthRepository {

    override suspend fun sendOtp(email: String) {
        service.sendOtp(email)
    }

    override suspend fun verifyOtp(
        email: String,
        code: String
    ): User {
        return service.verifyOtp(email = email, code = code).toDomain()
    }

    override suspend fun logout() {
        service.logout()
    }

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        sellerType: SellerType
    ) {
        service.signUp(
            email = email,
            password = password,
            firstName = firstName,
            sellerType = sellerType
            )
    }

    override suspend fun signIn(email: String, password: String): AuthUser {
        return service.signIn(email, password).toAuthUser()
    }

    override suspend fun getCurrentUser(): AuthUser? {
        val user = service.getCurrentUser() ?: return null
        return user.toAuthUser()
    }

    override suspend fun updatePassword(newPassword: String) {
        service.updatePassword(newPassword)
    }

    override suspend fun requestEmailChange(newEmail: String) {
        service.requestEmailChange(newEmail)
    }

    override suspend fun refreshAuthSession() {
        service.refreshAuthSession()
    }

    override suspend fun retrieveCurrentUserFromServer(): AuthUser? {
        return service.retrieveCurrentUserFromServer()?.toAuthUser()
    }
}