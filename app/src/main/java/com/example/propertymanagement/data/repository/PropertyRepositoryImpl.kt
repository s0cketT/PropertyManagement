package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.mapper.toDomain
import com.example.propertymanagement.data.mapper.toFullDto
import com.example.propertymanagement.data.mapper.toUpdateDto
import com.example.propertymanagement.data.model.CreateImageRequestDto
import com.example.propertymanagement.data.model.PropertyApplicationInsertDto
import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.repository.IPropertyRepository
import com.example.propertymanagement.domain.repository.IStorageRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PropertyRepositoryImpl(
    private val supabaseApi: ISupabaseApi,
    private val storageRepository: IStorageRepository
) : IPropertyRepository {

    override suspend fun getProperties(userId: String?): List<Property> {
        return supabaseApi.getProperties(userId).map { it.toDomain() }
    }

    override suspend fun getMyProperties(userId: String): List<Property> {
        return supabaseApi.getMyProperties(userId).map { it.toDomain() }
    }

    override suspend fun createProperty(
        request: CreateProperty
    ): Int {
        return supabaseApi.createFullProperty(
            request.toFullDto()
        )
    }

    override suspend fun updateFullProperty(
        propertyId: Int,
        request: CreateProperty,
    ) {
        val response = supabaseApi.updateFullProperty(request.toUpdateDto(propertyId))
        if (!response.isSuccessful) {
            val detail = response.errorBody()?.use { it.string() }.orEmpty()
            throw IllegalStateException(
                "update_full_property failed: HTTP ${response.code()} $detail",
            )
        }
    }

    override suspend fun saveImages(
        propertyId: Int,
        imageUrls: List<String>
    ) = coroutineScope {

        imageUrls.map { url ->
            async {
                supabaseApi.createImage(
                    CreateImageRequestDto(
                        property_id = propertyId,
                        url = url
                    )
                )
            }
        }.awaitAll()

        Unit
    }

    override suspend fun clearPropertyImages(propertyId: Int) = coroutineScope {

        val images = supabaseApi.getImagesByPropertyId("eq.$propertyId")

        images.map { image ->
            async {
                runCatching {
                    storageRepository.deleteImage(image.url)
                }
            }
        }.awaitAll()

        supabaseApi.deletePropertyImages("eq.$propertyId")
    }

    override suspend fun deleteProperty(propertyId: Int) = coroutineScope {

        val images = supabaseApi.getImagesByPropertyId("eq.$propertyId")

        images.map { image ->
            async {
                runCatching {
                    storageRepository.deleteImage(image.url)
                }
            }
        }.awaitAll()

        supabaseApi.deleteProperty("eq.$propertyId")
    }

    override suspend fun submitPropertyApplication(
        propertyId: Int,
        applicantUserId: String,
        comment: String?
    ) {
        supabaseApi.createPropertyApplication(
            PropertyApplicationInsertDto(
                property_id = propertyId,
                applicant_user_id = applicantUserId,
                comment = comment
            )
        )
    }
}