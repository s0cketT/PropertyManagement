package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.IUserRepository

class SaveAppRatingUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(stars: Int) {
        userRepository.updateAppRating(stars)
    }
}
