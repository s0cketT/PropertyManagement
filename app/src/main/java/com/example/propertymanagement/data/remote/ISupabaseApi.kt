package com.example.propertymanagement.data.remote

import com.example.propertymanagement.data.model.CheckEmailRequest
import com.example.propertymanagement.data.model.CityDto
import com.example.propertymanagement.data.model.CreateFullPropertyDto
import com.example.propertymanagement.data.model.UpdateFullPropertyDto
import com.example.propertymanagement.data.model.CreateImageRequestDto
import com.example.propertymanagement.data.model.ExistsResult
import com.example.propertymanagement.data.model.FavoriteDto
import com.example.propertymanagement.data.model.MyUserProfileResponseDto
import com.example.propertymanagement.data.model.PropertyApplicationInsertDto
import com.example.propertymanagement.data.model.PropertyApplicationResponseDto
import com.example.propertymanagement.data.model.PlatformManagerCommissionDto
import com.example.propertymanagement.data.model.PropertyImageDto
import com.example.propertymanagement.data.model.PropertyIdOnlyDto
import com.example.propertymanagement.data.model.PropertyResponseDto
import com.example.propertymanagement.data.model.RegionDto
import com.example.propertymanagement.data.model.ToggleFavoriteBody
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ISupabaseApi {

    @GET("region")
    suspend fun getRegions(
        @Query("select") select: String = "id,name",
        @Query("country_name") countryName: String = "eq.Беларусь",
        @Query("order") order: String = "name.asc"
    ): List<RegionDto>

    @GET("cities")
    suspend fun getCitiesByRegion(
        @Query("region_id") regionId: String,
        @Query("select") select: String = "id,region_id,name,lat,lng",
        @Query("order") order: String = "name.asc"
    ): List<CityDto>

    @POST("rpc/check_email_exists")
    suspend fun checkEmailExists(@Body body: CheckEmailRequest): List<ExistsResult>

    @GET("rpc/get_my_user_profile")
    suspend fun getMyUserProfile(
        @Query("p_user_uuid") userId: String,
    ): List<MyUserProfileResponseDto>

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

    @GET("platform_manager_commission_settings")
    suspend fun getPlatformManagerCommissionSettings(
        @Query("id") id: String = "eq.1",
        @Query("select") select: String = "commission_percent",
    ): List<PlatformManagerCommissionDto>

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

    /** Id одобренных объявлений для гостя (RLS «Public read approved properties for catalog»). */
    @GET("properties")
    suspend fun getApprovedCatalogPropertyIds(
        @Query("select") select: String = "id",
        @Query("moderation_status_id") moderationStatusId: String = "eq.3",
    ): List<PropertyIdOnlyDto>

    @GET("rpc/get_my_properties_with_favorite")
    suspend fun getMyProperties(
        @Query("p_user_uuid") userId: String
    ): List<PropertyResponseDto>

    @GET("rpc/get_my_property_applications")
    suspend fun getMyPropertyApplications(
        @Query("p_user_uuid") userId: String,
    ): List<PropertyApplicationResponseDto>
}
