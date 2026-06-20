package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.common.PropertyCatalogLog
import com.example.propertymanagement.data.mapper.toDomain
import com.example.propertymanagement.data.mapper.toFullDto
import com.example.propertymanagement.data.mapper.toUpdateDto
import com.example.propertymanagement.data.model.CreateImageRequestDto
import com.example.propertymanagement.data.model.PropertyApplicationInsertDto
import com.example.propertymanagement.data.model.PropertyIdOnlyDto
import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.domain.model.CreateProperty
import com.example.propertymanagement.domain.model.ModerationStatus
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.isApprovedForPublicCatalog
import com.example.propertymanagement.domain.model.visibleInPublicCatalog
import com.example.propertymanagement.domain.repository.IPropertyRepository
import com.example.propertymanagement.domain.repository.IStorageRepository
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PropertyRepositoryImpl(
    private val supabaseApi: ISupabaseApi,
    private val storageRepository: IStorageRepository
) : IPropertyRepository {

    override suspend fun getProperties(userId: String?): List<Property> {
        PropertyCatalogLog.authMode(isGuest = userId == null, userId = userId)

        val body = JsonObject()
        if (userId != null) {
            body.addProperty("p_user_uuid", userId)
        } else {
            body.add("p_user_uuid", JsonNull.INSTANCE)
        }

        val dtos = supabaseApi.getProperties(body)
        PropertyCatalogLog.rpcRawResponse(userId = userId, dtos = dtos)

        val mapped = dtos.map { it.toDomain() }
        PropertyCatalogLog.mappedProperties(userId = userId, properties = mapped)

        val withGuestFallback = if (userId == null) {
            refineGuestCatalogWhenStatusMissing(mapped)
        } else {
            mapped
        }

        val catalog = withGuestFallback.visibleInPublicCatalog()
        PropertyCatalogLog.catalogFilter(
            userId = userId,
            beforeCount = withGuestFallback.size,
            afterCount = catalog.size,
            removed = withGuestFallback.filterNot { it.isApprovedForPublicCatalog() },
        )

        return catalog
    }

    /**
     * Старый RPC на Supabase отдаёт все объявления без moderation_status / moderation_status_id.
     * Для гостя уточняем id через RLS-политику «Public read approved properties for catalog».
     */
    private suspend fun refineGuestCatalogWhenStatusMissing(
        properties: List<Property>,
    ): List<Property> {
        if (properties.isEmpty()) {
            return properties
        }

        val allStatusMissing = properties.all { property ->
            property.moderationStatus == ModerationStatus.UNKNOWN &&
                property.moderationStatusId == null
        }
        if (!allStatusMissing) {
            return properties
        }

        PropertyCatalogLog.rpcLegacyServerResponseWarning(properties.size)

        val approvedIds = runCatching {
            supabaseApi.getApprovedCatalogPropertyIds().map { it.id }.toSet()
        }.getOrElse { error ->
            PropertyCatalogLog.catalogError(
                userId = null,
                message = "getApprovedCatalogPropertyIds failed: ${error.message}",
                throwable = error,
            )
            emptySet()
        }

        PropertyCatalogLog.guestApprovedIdsFromRls(approvedIds.size)

        if (approvedIds.isEmpty()) {
            PropertyCatalogLog.catalogError(
                userId = null,
                message = "Guest catalog: RLS returned 0 approved ids. " +
                    "Deploy sql/moderation_status_catalog_helpers.sql, " +
                    "sql/rpc_get_properties_with_favorite.sql, " +
                    "sql/rls_properties_select_approved_for_catalog.sql",
            )
            return emptyList()
        }

        val refined = properties
            .filter { it.id in approvedIds }
            .map { property ->
                property.copy(
                    moderationStatus = ModerationStatus.APPROVED,
                    moderationStatusId = ModerationStatus.APPROVED_STATUS_ID,
                )
            }

        PropertyCatalogLog.guestCatalogRefined(
            rpcCount = properties.size,
            approvedCount = refined.size,
        )

        return refined
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

    override suspend fun getMyPropertyApplications(userId: String) =
        supabaseApi.getMyPropertyApplications(userId).map { it.toDomain() }
}