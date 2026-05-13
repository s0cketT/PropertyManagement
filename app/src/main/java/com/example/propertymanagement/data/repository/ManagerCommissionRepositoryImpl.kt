package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.domain.common.Resource
import com.example.propertymanagement.domain.model.ExceptionDomainModel
import com.example.propertymanagement.domain.repository.IManagerCommissionRepository

class ManagerCommissionRepositoryImpl(
    private val supabaseApi: ISupabaseApi,
) : IManagerCommissionRepository {

    @Volatile
    private var cachedPercent: Double? = null

    override suspend fun fetchCommissionPercent(): Resource<Double, ExceptionDomainModel> {
        cachedPercent?.let {
            return Resource.Success<Double, ExceptionDomainModel>(it)
        }
        return runCatching {
            val rows = supabaseApi.getPlatformManagerCommissionSettings()
            val raw = rows.firstOrNull()?.commission_percent
                ?: throw IllegalStateException("platform_manager_commission_settings row missing")
            if (raw < 0) {
                throw IllegalStateException("Invalid commission_percent: $raw")
            }
            raw
        }.fold(
            onSuccess = { value ->
                cachedPercent = value
                Resource.Success<Double, ExceptionDomainModel>(value)
            },
            onFailure = { cause ->
                Resource.Error(
                    ExceptionDomainModel.from(
                        IllegalStateException("Failed to load manager commission", cause),
                    ),
                )
            },
        )
    }
}
