package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.repository.IFavoriteRepository

class ToggleFavoriteUseCase(
    private val favoriteRepository: IFavoriteRepository
) {
    suspend operator fun invoke(userId: String, propertyId: Int) {
        favoriteRepository.toggleFavorite(userId, propertyId)
    }
}