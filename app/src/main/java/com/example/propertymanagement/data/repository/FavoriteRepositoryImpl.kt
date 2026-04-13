package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.model.ToggleFavoriteBody
import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.domain.repository.IFavoriteRepository
import retrofit2.HttpException

class FavoriteRepositoryImpl(
    private val supabaseApi: ISupabaseApi
) : IFavoriteRepository {

    override suspend fun toggleFavorite(userId: String, propertyId: Int) {
        val body = ToggleFavoriteBody(
            p_user_uuid = userId,
            p_property_id = propertyId
        )

        val response = supabaseApi.toggleFavorite(body)

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    override suspend fun isFavorite(userId: String, propertyId: Int): Boolean {
        return supabaseApi.getFavorite(userId, propertyId).isNotEmpty()
    }
}