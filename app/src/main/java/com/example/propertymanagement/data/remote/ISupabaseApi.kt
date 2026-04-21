package com.example.propertymanagement.data.remote

import com.example.propertymanagement.data.model.CheckEmailRequest
import com.example.propertymanagement.data.model.CreateFullPropertyDto
import com.example.propertymanagement.data.model.UpdateFullPropertyDto
import com.example.propertymanagement.data.model.CreateImageRequestDto
import com.example.propertymanagement.data.model.ExistsResult
import com.example.propertymanagement.data.model.FavoriteDto
import com.example.propertymanagement.data.model.PropertyApplicationInsertDto
import com.example.propertymanagement.data.model.PropertyImageDto
import com.example.propertymanagement.data.model.PropertyResponseDto
import com.example.propertymanagement.data.model.ToggleFavoriteBody
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
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

    @POST("rpc/update_full_property")
    suspend fun updateFullProperty(
        @Body body: UpdateFullPropertyDto,
    ): Response<Unit>

    @POST("property_applications")
    suspend fun createPropertyApplication(
        @Body body: PropertyApplicationInsertDto
    )

    @DELETE("properties")
    suspend fun deleteProperty(
        @Query("id") id: String
    )

    @DELETE("property_images")
    suspend fun deletePropertyImages(
        @Query("property_id") propertyId: String,
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

    @POST("rpc/get_properties_with_favorite")
    suspend fun getProperties(
        @Body body: JsonObject,
    ): List<PropertyResponseDto>

    @GET("rpc/get_my_properties_with_favorite")
    suspend fun getMyProperties(
        @Query("p_user_uuid") userId: String
    ): List<PropertyResponseDto>
}
