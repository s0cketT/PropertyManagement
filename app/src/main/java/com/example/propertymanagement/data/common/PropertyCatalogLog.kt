package com.example.propertymanagement.data.common

import android.util.Log
import com.example.propertymanagement.data.model.PropertyResponseDto
import com.example.propertymanagement.domain.model.Property

object PropertyCatalogLog {

    const val TAG = "PropertyCatalog"

    fun authMode(isGuest: Boolean, userId: String?) {
        Log.d(
            TAG,
            "authMode: guest=$isGuest userId=${userId ?: "null"}",
        )
    }

    fun restRequest(path: String, isGuestSession: Boolean) {
        Log.d(
            TAG,
            "restRequest: path=$path guestSession=$isGuestSession",
        )
    }

    fun rpcRawResponse(userId: String?, dtos: List<PropertyResponseDto>) {
        Log.d(
            TAG,
            "rpcRawResponse: userId=${userId ?: "null (guest)"} dtoCount=${dtos.size}",
        )
        if (dtos.isEmpty()) {
            Log.w(TAG, "rpcRawResponse: empty list from get_properties_with_favorite")
            return
        }

        val histogram = dtos.groupingBy { dto ->
            "${dto.moderation_status ?: "null"}|${dto.moderation_status_id ?: "null"}"
        }.eachCount()
        Log.d(TAG, "rpcRawResponse status histogram: $histogram")

        val sample = dtos.take(5).joinToString(separator = "; ") { dto ->
            "id=${dto.id} status=${dto.moderation_status} statusId=${dto.moderation_status_id}"
        }
        Log.d(TAG, "rpcRawResponse sample: $sample")

        if (dtos.isNotEmpty() && dtos.all { it.moderation_status.isNullOrBlank() && it.moderation_status_id == null }) {
            Log.w(
                TAG,
                "rpcRawResponse: moderation_status and moderation_status_id are NULL for ALL rows. " +
                    "Supabase likely runs old get_properties_with_favorite. " +
                    "Redeploy sql/moderation_status_catalog_helpers.sql and " +
                    "sql/rpc_get_properties_with_favorite.sql. Guest fallback uses RLS ids.",
            )
        }
    }

    fun rpcLegacyServerResponseWarning(rpcCount: Int) {
        Log.w(
            TAG,
            "rpcLegacyServerResponseWarning: RPC returned $rpcCount rows without moderation fields",
        )
    }

    fun guestApprovedIdsFromRls(count: Int) {
        Log.d(TAG, "guestApprovedIdsFromRls: approvedIdCount=$count")
    }

    fun guestCatalogRefined(rpcCount: Int, approvedCount: Int) {
        Log.d(
            TAG,
            "guestCatalogRefined: rpcCount=$rpcCount approvedAfterRls=$approvedCount " +
                "hidden=${rpcCount - approvedCount}",
        )
    }

    fun mappedProperties(userId: String?, properties: List<Property>) {
        val statusCounts = properties.groupingBy { it.moderationStatus }.eachCount()
        val idCounts = properties.groupingBy { it.moderationStatusId }.eachCount()
        Log.d(
            TAG,
            "mappedProperties: userId=${userId ?: "null (guest)"} count=${properties.size} " +
                "moderationStatus=$statusCounts moderationStatusId=$idCounts",
        )
    }

    fun catalogFilter(
        userId: String?,
        beforeCount: Int,
        afterCount: Int,
        removed: List<Property>,
    ) {
        Log.d(
            TAG,
            "catalogFilter: userId=${userId ?: "null (guest)"} before=$beforeCount after=$afterCount " +
                "removed=${removed.size}",
        )
        if (beforeCount > 0 && afterCount == 0) {
            val removedSample = removed.take(5).joinToString(separator = "; ") { property ->
                "id=${property.id} status=${property.moderationStatus} statusId=${property.moderationStatusId}"
            }
            Log.w(
                TAG,
                "catalogFilter removed ALL items — check RPC/SQL or filter rules. Sample: $removedSample",
            )
        }
    }

    fun catalogError(userId: String?, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(TAG, "catalogError: userId=${userId ?: "null (guest)"} $message", throwable)
        } else {
            Log.e(TAG, "catalogError: userId=${userId ?: "null (guest)"} $message")
        }
    }

    fun uiState(
        userId: String?,
        propertiesCount: Int,
        propertiesFilterCount: Int,
        hasActiveFilters: Boolean,
        searchQuery: String,
    ) {
        Log.d(
            TAG,
            "uiState: userId=${userId ?: "null (guest)"} properties=$propertiesCount " +
                "propertiesFilter=$propertiesFilterCount activeFilters=$hasActiveFilters " +
                "searchQuery=\"$searchQuery\"",
        )
    }

    fun logRejectedHidden(property: Property) {
        Log.d(
            TAG,
            "catalogFilter hide id=${property.id} status=${property.moderationStatus} " +
                "statusId=${property.moderationStatusId}",
        )
    }
}
