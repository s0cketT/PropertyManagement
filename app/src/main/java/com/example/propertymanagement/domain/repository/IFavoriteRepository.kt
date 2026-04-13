package com.example.propertymanagement.domain.repository

interface IFavoriteRepository {
    suspend fun toggleFavorite(userId: String, propertyId: Int)
    suspend fun isFavorite(userId: String, propertyId: Int): Boolean
}