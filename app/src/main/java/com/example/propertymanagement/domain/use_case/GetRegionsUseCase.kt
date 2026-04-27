package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.Region
import com.example.propertymanagement.domain.repository.IRegionCityRepository

class GetRegionsUseCase(
    private val regionCityRepository: IRegionCityRepository,
) {

    suspend operator fun invoke(): List<Region> {
        return regionCityRepository.getRegions()
    }
}

