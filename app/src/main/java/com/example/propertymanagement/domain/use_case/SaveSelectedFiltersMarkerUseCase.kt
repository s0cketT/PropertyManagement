package com.example.propertymanagement.domain.use_case

import com.example.propertymanagement.domain.model.FiltersProperty
import com.example.propertymanagement.domain.repository.IFiltersRepository

class SaveSelectedFiltersMarkerUseCase(
    private val filterRepository: IFiltersRepository
) {

    suspend operator fun invoke(marker: FiltersProperty) {
        filterRepository.saveSelectedFiltersMarker(marker)
    }

}