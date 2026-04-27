package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.City
import com.example.propertymanagement.domain.repository.IRegionCityRepository

class GetCitiesByRegionUseCase(
    private val regionCityRepository: IRegionCityRepository,
) {

    suspend operator fun invoke(regionId: Long): List<City> {
        return regionCityRepository.getCitiesByRegion(regionId = regionId)
    }
}

