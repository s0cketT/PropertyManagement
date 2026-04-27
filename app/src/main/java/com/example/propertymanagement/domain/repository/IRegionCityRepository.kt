package com.example.propertymanagement.domain.repository

import com.example.propertymanagement.domain.model.City
import com.example.propertymanagement.domain.model.Region

interface IRegionCityRepository {
    suspend fun getRegions(): List<Region>
    suspend fun getCitiesByRegion(regionId: Long): List<City>
}

