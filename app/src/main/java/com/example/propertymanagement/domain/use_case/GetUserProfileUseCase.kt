package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.domain.repository.IUserRepository

class GetUserProfileUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(userId: String): UserProfile {
        return userRepository.getUserProfile(userId)
    }
}