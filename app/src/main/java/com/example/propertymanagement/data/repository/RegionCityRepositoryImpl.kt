package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.mapper.toDomain
import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.domain.model.City
import com.example.propertymanagement.domain.model.Region
import com.example.propertymanagement.domain.repository.IRegionCityRepository

class RegionCityRepositoryImpl(
    private val supabaseApi: ISupabaseApi,
) : IRegionCityRepository {

    override suspend fun getRegions(): List<Region> {
        return supabaseApi.getRegions().map { regionDto ->
            regionDto.toDomain()
        }
    }

    override suspend fun getCitiesByRegion(regionId: Long): List<City> {
        return supabaseApi.getCitiesByRegion(
            regionId = "eq.$regionId",
        ).map { cityDto ->
            cityDto.toDomain()
        }
    }
}

