package com.example.propertymanagement.data.remote

import com.example.propertymanagement.data.model.CheckEmailRequest
import com.example.propertymanagement.data.model.CreateFullPropertyDto
import com.example.propertymanagement.data.model.CreateImageRequestDto
import com.example.propertymanagement.data.model.ExistsResult
import com.example.propertymanagement.data.model.FavoriteDto
import com.example.propertymanagement.data.model.PropertyImageDto
import com.example.propertymanagement.data.model.PropertyResponseDto
import com.example.propertymanagement.data.model.ToggleFavoriteBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ISupabaseApi {

    @POST("rpc/check_email_exists")
    suspend fun checkEmailExists(@Body body: CheckEmailRequest): List<ExistsResult>

    @POST("property_images")
    suspend fun createImage(
        @Body body: CreateImageRequestDto
    )

    @POST("rpc/create_full_property")
    suspend fun createFullProperty(
        @Body body: CreateFullPropertyDto
    ): Int

    @DELETE("properties")
    suspend fun deleteProperty(
        @Query("id") id: String
    )

    @GET("property_images")
    suspend fun getImagesByPropertyId(
        @Query("property_id") propertyId: String
    ): List<PropertyImageDto>

    @GET("favorite_properties")
    suspend fun getFavorite(
        @Query("user_id") userId: String,
        @Query("property_id") propertyId: Int
    ): List<FavoriteDto>

    @POST("rpc/toggle_favorite")
    suspend fun toggleFavorite(
        @Body body: ToggleFavoriteBody
    ): Response<Unit>

    @GET("rpc/get_properties_with_favorite")
    suspend fun getProperties(
        @Query("p_user_uuid") userId: String?
    ): List<PropertyResponseDto>
}
