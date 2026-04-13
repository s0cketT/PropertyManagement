package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.UserProfile

interface IUserRepository {
    suspend fun isUserExists(email: String): Boolean

    suspend fun getUserProfile(userId: String): UserProfile

    suspend fun updateAvatar(url: String?)

    suspend fun getCurrentAvatarUrl(): String?
}